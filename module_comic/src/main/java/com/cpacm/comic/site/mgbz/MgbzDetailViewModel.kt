package com.cpacm.comic.site.mgbz

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.http.MgbzRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchActivity
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *     详情页
 * @author cpacm 2019-12-20
 */
class MgbzDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetailBean = withRequest {
                val response = MgbzRetrofitHelper.instance.mgbzApis.getComicDetail(comicId)
                val body = ComicElement(response.string())

                val comicBean = ComicDetailBean()
                comicBean.id = comicId
                val div = body.get().select("div.detail-info-1 div.container div.detail-info")
                comicBean.cover = div.select("img.detail-info-cover").attr("src")
                comicBean.title = div.select("p.detail-info-title").text().trim()

                comicBean.hot = div.select("p.detail-info-stars span").text().trim()
                val info = div.select("p.detail-info-tip span")
                var authorStr = "作者："
                val authors = info[0].select("a")
                for (i in 0 until authors.size) {
                    authorStr += authors[i].text().trim()
                    if (i < authors.size - 1) {
                        authorStr += "  "
                    }
                }
                comicBean.authors = authorStr

                comicBean.status = info[1].text().trim()
                comicBean.category = info[2].text().trim()

                comicBean.description = body.get().select("div.detail-info-2 div.container div.detail-info p.detail-info-content").text().trim()
                val comicVolumeList = arrayListOf<ComicVolumeBean>()
                val volumeInfo = body.get().select("div.container div.detail-list-form-con")
                val volume = ComicVolumeBean()
                volume.title = "章节列表"
                val chapterList = arrayListOf<ComicChapterBean>()
                val list = volumeInfo.select("a")
                for (cpEle in list) {
                    val chapter = ComicChapterBean()
                    chapter.isVolume = false
                    chapter.chapterTitle = cpEle.text().trim()
                    val chapterNum = cpEle.select("a").attr("href").replace("/", "").replace("m", "")
                    chapter.chapterId = chapterNum
                    chapterList.add(chapter)
                }
                volume.content = chapterList
                comicVolumeList.add(volume)

                comicBean.chapters = comicVolumeList
                comicBean.webKey = ComicSiteFactory.COMIC_MGBZ
                comicBean
            }
            comic.postValue(comicDetailBean)
        }
    }

    override fun setAuthorLink(context: Context, str: String): SpannableString {
        val ss = SpannableString(str)
        if (!str.contains("作者")) return ss
        val list = str.substring(3, str.length).trim().split("  ")
        var index = 3
        for (s in list) {
            ss.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ComicSearchActivity.startKeyword(context, s, 3)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

            }, index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.link_color)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //ss.setSpan(BackgroundColorSpan(resources.getColor(R.color.colorAccent)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            index += s.length + 2

        }
        return ss
    }

    override fun isReverse(): Boolean {
        return true
    }

}