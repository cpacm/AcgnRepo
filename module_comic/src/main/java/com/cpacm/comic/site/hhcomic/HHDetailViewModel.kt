package com.cpacm.comic.site.hhcomic

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.http.HHRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.utils.ComicElement
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * <p>
 *     HH详情页
 * @author cpacm 2019-12-06
 */
class HHDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetailBean = withRequest {
                val response = HHRetrofitHelper.instance.hhApi.getHHComic(comicId)
                val body = ComicElement(response.string())
                val main = body.get().select("div#list div.product")
                val comicBean = ComicDetailBean()
                comicBean.id = comicId
                comicBean.cover = main.select("div#about_style > img").attr("src")
                val infos = main.select("div#about_kit > ul > li")
                for (info in infos) {
                    if (infos.indexOf(info) == 0) {
                        comicBean.title = info.select("h1").`val`().trim()
                        continue
                    }
                    val value = info.text()
                    if (value.contains("作者")) {
                        comicBean.authors = value
                    } else if (value.contains("状态")) {
                        comicBean.status = value
                    } else if (value.contains("集数")) {
                        comicBean.category = value
                    } else if (value.contains("评价")) {
                        comicBean.hot = value
                    } else if (value.contains("简介")) {
                        comicBean.description = value
                    }
                }

                var volume: ComicVolumeBean? = null
                var vList = arrayListOf<ComicVolumeBean>()
                val list = main.select("div.cVolList")[0]
                for (ele in list.children()) {
                    if (ele.tagName() == "div") {
                        volume = ComicVolumeBean()
                        val name = ele.text()
                        volume.title = name
                        volume.content = arrayListOf()
                        vList.add(volume)
                        continue
                    } else if (ele.tagName() == "ul") {
                        val cs = ele.select("li")
                        val chapterList = arrayListOf<ComicChapterBean>()
                        for (c in cs) {
                            val chapter = ComicChapterBean()
                            val name = c.select("a").text().trim()
                            val url = c.select("a").attr("href")
                            val array = match("/cool(\\d+).*s=(\\d+)", url, 1, 2)

                            chapter.isVolume = false
                            chapter.chapterTitle = name
                            chapter.chapterId = array!![0].toString()
                            chapter.url = array[1].toString()
                            chapterList.add(chapter)
                        }
                        if (volume == null) {
                            volume = ComicVolumeBean()
                            volume.title = "未定"
                            volume.content = arrayListOf()
                            vList.add(volume)
                        }
                        volume.content!!.addAll(chapterList)
                    }
                }
                comicBean.chapters = vList
                comicBean.webKey = ComicSiteFactory.COMIC_HH
                comicBean
            }
            comic.postValue(comicDetailBean)
        }
    }

    override fun isReverse(): Boolean {
        return true
    }

    fun match(regex: String?, input: String?, vararg group: Int): Array<String?>? {
        try {
            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(input)
            if (matcher.find()) {
                val result = arrayOfNulls<String>(group.size)
                for (i in result.indices) {
                    result[i] = matcher.group(group[i])
                }
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}