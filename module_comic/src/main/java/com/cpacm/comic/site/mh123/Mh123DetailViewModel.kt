package com.cpacm.comic.site.mh123

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.http.Mh123RetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.utils.ComicElement
import java.util.regex.Pattern

/**
 * <p>
 *     详情页
 * @author cpacm 2019-12-20
 */
class Mh123DetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetailBean = withRequest {
                val response = Mh123RetrofitHelper.instance.mh123Apis.getComicDetail(comicId)
                val body = ComicElement(response.string())

                val comicBean = ComicDetailBean()
                comicBean.id = comicId
                val div = body.get().select("div.content div.maininfo")
                comicBean.cover = div.select("div.bpic img").attr("src")
                val info = div.select("div.info ul li")
                comicBean.title = info.select("h2").text()
                comicBean.status = info[1].select("p em").text().trim()
                comicBean.category = info[2].select("p a").text().trim()
                comicBean.authors = info[3].select("p").text().trim()
                comicBean.hot = info[5].select("p").text().trim()
                comicBean.description = div.select("div.mt10").text().trim()
                val comicVolumeList = arrayListOf<ComicVolumeBean>()
                val volumeInfo = body.get().select("div#js_tab div.yuanbox_c")
                val volume = ComicVolumeBean()
                volume.title = volumeInfo.select("div.title01 h2").text()
                val chapterList = arrayListOf<ComicChapterBean>()
                val list = volumeInfo.select("ul.jslist01 > li")
                for (cpEle in list) {
                    val chapter = ComicChapterBean()
                    chapter.isVolume = false
                    chapter.chapterTitle = cpEle.select("a").text()
                    val chapterNum = cpEle.select("a").attr("href").split("/").last().replace(".html", "")
                    chapter.chapterId = chapterNum
                    chapterList.add(chapter)
                }
                volume.content = chapterList
                comicVolumeList.add(volume)

                comicBean.chapters = comicVolumeList
                comicBean.webKey = ComicSiteFactory.COMIC_MH123
                comicBean
            }
            comic.postValue(comicDetailBean)
        }
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