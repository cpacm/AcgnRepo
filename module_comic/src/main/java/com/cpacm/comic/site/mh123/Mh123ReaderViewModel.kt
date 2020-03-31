package com.cpacm.comic.site.mh123

import android.util.Base64
import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.Mh123RetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import org.jsoup.Jsoup
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
class Mh123ReaderViewModel : ComicReaderViewModel() {


    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val imgList = withRequest {
                val response = Mh123RetrofitHelper.instance.mh123Apis.getComicContent(comicId, chapterBean.chapterId
                        ?: "")
                val string = response.string()
                var encryptedData = ""
                val scripts = Jsoup.parse(string).select("script")
                for (script in scripts) {
                    if (script.data().startsWith("var")) {
                        encryptedData = script.data()
                        break
                    }
                }
                /**
                 * var z_yurl='https://img.czxrsp.com/';
                 * var z_img='["upload\/files\/22002\/941842\/154385337819.jpg","upload\/files\/22002\/941842\/154385337820.jpg"];
                 * var lock='0';
                 * var play='gufeng';
                 */
                val links = arrayListOf<String>()
                var server = "https://img.czxrsp.com/"
                val list = encryptedData.split(";")
                for (param in list) {
                    if (param.contains("z_yurl")) {
                        val reg = "\"(.*?)\""
                        server = match(reg, param, 1) ?: "https://img.czxrsp.com/"
                    } else if (param.contains("z_img")) {
                        val reg = "\"(.*?)\""
                        val paths = match(reg, param)
                        for (ele in paths) {
                            links.add(ele.replace("\\", ""))
                        }
                    }
                }
                for (i in 0 until links.size) {
                    if (links[i].startsWith("http")) continue
                    links[i] = server + links[i]
                }
                links
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

    fun match(regex: String?, input: String?): List<String> {
        val list = arrayListOf<String>()
        try {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(input)
            while (matcher.find()) {
                list.add(matcher.group(1).trim())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}