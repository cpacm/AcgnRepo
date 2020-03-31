package com.cpacm.comic.site.xxmh

import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.XXmhRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.utils.JsPackerDecoder
import org.jsoup.Jsoup
import java.util.regex.Pattern

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class XXmhReaderViewModel : ComicReaderViewModel() {

    private val serverMap = hashMapOf<String, String>()
    private var isWebP = false

    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val path = chapterBean.url.substring(1, chapterBean.url.length).replace(".html", "")
            val imgList = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.getXXComicContent(path)
                val string = response.string()
                val encryptedData = Jsoup.parse(string).select("script")[0].data()

                val dataStr = JsPackerDecoder.getDecodeJS(encryptedData)
                /*
                开始解析参数
                var atsvr="gn";
                var msg=\'202001/09/2035382720.jpg|202001/09/2035383341.jpg|202001/09/2035384712.jpg|202001/09/2035385163.jpg|202001/09/2035381194.jpg|202001/09/2035383575.jpg|202001/09/2035381036.jpg|202001/09/2035386677.jpg|202001/09/2035383428.jpg|202001/09/2035388529.jpg|202001/09/20353894210.jpg|202001/09/20353844211.jpg|202001/09/20353839812.jpg|202001/09/20353852313.jpg|202001/09/20353817914.jpg|202001/09/20353884415.jpg|202001/09/20353834716.jpg|202001/09/203538100017.jpg\';
                var maxPage=18;
                var img_s=51;
                var preLink_b=\'\';
                var preName_b=\'\';
                var nextLink_b=\'https://www.177mh.net/202001/437468.html\';
                var nextName_b=\'第02话\';var linkname=\'第01话\';
                var link_z=\'https://www.177mh.net/colist_244253.html\';
                var linkn_z=\'异世界学姐 -魔术学姐在这个世界貌似也是废柴-\';
                 */
                var atsvr = "gn"
                var img_s = "1"
                val links = arrayListOf<String>()
                val list = dataStr.split(";")
                for (param in list) {
                    if (param.contains("atsvr")) {
                        val reg = "\"(.*?)\""
                        atsvr = match(reg, param, 1) ?: "gn"
                    } else if (param.contains("img_s")) {
                        img_s = param.split("=").last()
                    } else if (param.contains("msg")) {
                        val reg = "\\'(.*?)\\'"
                        val paths = (match(reg, param, 1) ?: "").split("|")
                        for (ele in paths) {
                            if (ele.endsWith("\\")) {
                                links.add(ele.substring(0, ele.length - 1))
                            } else {
                                links.add(ele)
                            }
                        }
                    }
                }
                val server = getServer(atsvr, comicId, chapterBean.chapterId!!, img_s)
                for (i in 0 until links.size) {
                    links[i] = server + links[i]
                    if (isWebP) links[i] = links[i] + ".webp"
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


    /**
     * var img_qianzso = new Array();img_qianzso[51] = "https://picsh.77dm.top/h51/";var webpshow = 1;
     */
    suspend fun getServer(atsvr: String = "gn", comicId: String, chapterId: String, index: String): String {
        if (serverMap.containsKey(index)) return serverMap[index]!!

        val response = XXmhRetrofitHelper.instance.xxmhApis.xxServerList(atsvr, index, comicId, chapterId)
        val string = response.string()
        val list = string.split(";")
        for (server in list) {
            if (server.contains("webpshow")) {
                isWebP = server.split("=").last().replace(" ","").equals("1")
            } else {
                val reg1 = "\\[(.*?)\\]"
                val key = match(reg1, server, 1)
                val reg = "\"(.*?)\""
                val value = match(reg, server, 1)
                if (key != null && value != null) {
                    serverMap.put(key, value)
                }
            }
        }
        return serverMap[index]!!

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