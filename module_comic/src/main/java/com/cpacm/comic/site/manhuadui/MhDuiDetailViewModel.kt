package com.cpacm.comic.site.manhuadui

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.http.MhDuiApis
import com.cpacm.comic.model.http.MhDuiRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.utils.ComicElement
import java.util.regex.Pattern

/**
 * <p>
 *     详情页
 * @author cpacm 2019-12-20
 */
class MhDuiDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetailBean = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.getMHDuiComic(comicId)
                val body = ComicElement(response.string())
                val main = body.get().select("div.wrap")
                val comicBean = ComicDetailBean()
                comicBean.id = comicId
                val div = main.select("div.wrap_intro_l_comic")
                comicBean.cover = div.select("div.comic_i_img img").attr("src")
                comicBean.title = div.select("div.comic_deCon h1").text()
                val info = div.select("ul.comic_deCon_liO li")
                comicBean.authors = (info[0].text() ?: "未知").trim()
                comicBean.status = info[1].text().trim()
                comicBean.category = info[3].text().trim()
                comicBean.hot = div.select("ul.comic_deCon_liT li")[1].text().trim()
                comicBean.description = div.select("p.comic_deCon_d").text().trim()
                val comicVolumeList = arrayListOf<ComicVolumeBean>()
                val volumeList = body.list("div.zj_list")
                for (vList in volumeList) {
                    val head = vList.get().select("div.zj_list_head")
                    if (head.isEmpty()) continue
                    val volume = ComicVolumeBean()
                    volume.title = head.select("h2 em").text()
                    val chapterList = arrayListOf<ComicChapterBean>()
                    val list = vList.list("ul > li")
                    for (cpEle in list) {
                        val chapter = ComicChapterBean()
                        chapter.isVolume = false
                        chapter.chapterTitle = cpEle.attr("a", "title")
                        val url = MhDuiApis.BASE_URL2 + cpEle.attr("a", "href")
                        val chapterNum = match("/\\d+.html", cpEle.href("a"), 0)!!.filterDigital()
                        chapter.chapterId = chapterNum
                        chapter.url = url
                        chapterList.add(chapter)
                    }
                    volume.content = chapterList
                    comicVolumeList.add(volume)
                }
                comicBean.chapters = comicVolumeList
                comicBean.webKey = ComicSiteFactory.COMIC_MHDUI
                comicBean
            }
            comic.postValue(comicDetailBean)
        }
    }

    private fun String.filterDigital(): String {
        return this.replace("[^0-9]".toRegex(), "")
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

    override fun isReverse(): Boolean {
        return false
    }

}