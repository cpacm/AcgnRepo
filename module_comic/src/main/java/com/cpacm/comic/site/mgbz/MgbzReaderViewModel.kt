package com.cpacm.comic.site.mgbz

import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.MgbzApis
import com.cpacm.comic.model.http.MgbzRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.utils.JsPackerDecoder
import java.util.regex.Pattern

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class MgbzReaderViewModel : ComicReaderViewModel() {

    private var isLoop = false

    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val imgList = withRequest {
                var page = 1
                val data = ArrayList<String>()
                isLoop = true
                while (isLoop) {
                    val response = MgbzRetrofitHelper.instance.mgbzApis.getComicContent(MgbzApis.BASE_URL + "m${chapterBean.chapterId}/", chapterBean.chapterId!!, chapterBean.chapterId!!, page)
                    val string = response.string()
                    val dataStr = JsPackerDecoder.getDecodeJS(string)
                    /**
                     * 解析参数
                     * function dm5imagefun()
                     * {var cid=15968;
                     * var key=\'d40c0fc870ae92e0ae0e69d15fe6523e\';
                     * var pix="http://image.mangabz.com/1/236/15968";
                     * var pvalue=["/1_5317.jpg","/2_4470.jpg"];
                     * for(var i=0;i<pvalue.length;i++){pvalue[i]=pix+pvalue[i]+\'?cid=15968&key=d40c0fc870ae92e0ae0e69d15fe6523e&uk=\'}
                     * return pvalue}
                     * var d;
                     * d=dm5imagefun();
                     */
                    var server = ""
                    var lastfix = ""
                    val newImg = arrayListOf<String>()
                    val list = dataStr.split(";")
                    for (param in list) {
                        if (param.contains("var pix")) {
                            val reg = "\"(.*?)\""
                            server = match(reg, param, 1) ?: ""
                        } else if (param.contains("var pvalue")) {
                            val reg = "\"(.*?)\""
                            val paths = match(reg, param)
                            for (ele in paths) {
                                newImg.add(ele)
                            }
                        } else if (param.contains("?cid")) {
                            val reg = "\'(.*?)\'"
                            lastfix = match(reg, param, 1) ?: ""
                        }
                    }
                    for (img in newImg) {
                        val realImg = server + img + lastfix
                        if (data.contains(realImg)) {
                            isLoop = false
                            break
                        } else {
                            data.add(realImg)
                        }
                    }
                    page += newImg.size
                    if (newImg.size == 0) isLoop = false
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

    override fun onCleared() {
        super.onCleared()
        isLoop = false
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