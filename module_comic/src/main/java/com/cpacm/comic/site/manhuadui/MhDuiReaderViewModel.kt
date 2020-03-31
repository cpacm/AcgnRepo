package com.cpacm.comic.site.manhuadui

import android.util.Base64
import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.MhDuiRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import com.google.gson.Gson
import java.nio.charset.Charset
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class MhDuiReaderViewModel : ComicReaderViewModel() {


    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val imgList = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.getHHComicContent(comicId, chapterBean.chapterId
                        ?: "")
                val string = response.string()
                val encryptedData = match("var chapterImages = \"(.*?)\"", string, 1)!!

                val dataStr = decode(encryptedData)
                val list = Gson().fromJson(dataStr, Array<String>::class.java)
                val data = ArrayList<String>()
                for (item in list) {
                    if (item.startsWith("http")) {
                        data.add(item)
                    } else {
                        val path = match("var chapterPath = \"(.*?)\";", string, 1)
                        data.add("https://mhcdn.manhuazj.com/$path$item")
                    }
                }
                data
            }

            val comicContentBean = ComicContentBean()
            comicContentBean.title = chapterData[index].chapterTitle
            comicContentBean.picNum = imgList.size
            comicContentBean.comicId = comicId
            comicContentBean.chapterId = chapterData[index].chapterId ?: ""
            comicContentBean.pageUrl = imgList
            loadData(index, order, comicContentBean, offsetPage)
        }
    }

    fun decode(data: String): String {
        val key = "123456781234567G".toByteArray(Charset.forName("UTF-8"))
        val ivs = "ABCDEF1G34123412".toByteArray(Charset.forName("UTF-8"))
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        val paramSpec = IvParameterSpec(ivs)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec)
        return String(cipher.doFinal(Base64.decode(data, Base64.DEFAULT)))
    }

    fun match(regex: String?, input: String?, group: Int): String? {
        try {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(input)
            if (matcher.find()) {
                return matcher.group(group).trim { it <= ' ' }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}