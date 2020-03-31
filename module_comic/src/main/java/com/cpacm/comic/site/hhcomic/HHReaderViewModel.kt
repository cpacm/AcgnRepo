package com.cpacm.comic.site.hhcomic

import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.HHRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class HHReaderViewModel : ComicReaderViewModel() {

    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val imgList = withRequest {
                var count = 1
                var page = 1
                var list = arrayListOf<String>()
                while (page <= count) {
                    val response = HHRetrofitHelper.instance.hhApi.getHHComicContent(chapterBean.chapterId!!, page, chapterBean.url)
                    val string = response.string()
                    val body = ComicElement(string)
                    var server = body.attr("#hdDomain", "value")
                    if (server != null) {
                        server = server.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    }
                    count = body.attr("#hdPageCount", "value").toInt()

                    val name = body.attr("#iBodyQ > img", "name")
                    val result = unsuan(name).substring(1)
                    val cur = server + result
                    list.add(cur)
                    page += 1
                    if (page < count) {
                        val next = body.attr("#hdNextImg", "value")
                        list.add(server + unsuan(next).substring(1))
                        page += 1
                    }
                }
                list
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

    private fun unsuan(strFile: String): String {
        var str = strFile
        val num = str.length - str[str.length - 1].toInt() + 'a'.toInt()
        var code = str.substring(num - 13, num - 2)
        val cut = code.substring(code.length - 1)
        str = str.substring(0, num - 13)
        code = code.substring(0, code.length - 1)
        for (i in 0 until code.length) {
            str = str.replace(code[i], ('0'.toInt() + i).toChar())
        }
        val builder = StringBuilder()
        val array = str.split(cut)
        for (i in array.indices) {
            builder.append(Integer.parseInt(array[i]).toChar())
        }
        return builder.toString()
    }
}