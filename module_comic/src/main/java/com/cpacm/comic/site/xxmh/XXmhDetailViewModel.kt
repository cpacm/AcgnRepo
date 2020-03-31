package com.cpacm.comic.site.xxmh

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
import com.cpacm.comic.model.http.XXmhRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchActivity
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *     新新漫画详情页
 * @author cpacm 2019-12-06
 */
class XXmhDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetailBean = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.getXXComic(comicId)
                val body = ComicElement(response.string())
                val comicBean = ComicDetailBean()
                val main = body.get().select("div#main div.ar_list div.ar_list_coc")
                val cl = body.get().select("div#main div.ar_list h3 > a")
                var str = "分类："
                for (node in cl) {
                    str += node.text() + "  "
                }
                comicBean.category = str
                comicBean.id = comicId
                comicBean.cover = main.select("dt img").attr("src")
                val div = main.select("dd ul.ar_list_coc")
                comicBean.title = div.select("li h1").text()
                comicBean.authors = "作者：" + div.select("li")[1].select("a").text()
                comicBean.status = div.select("li")[2].select("a").text()
                comicBean.hot = div.select("li")[3].text()
                comicBean.description = div.select("li.sharer p i.d_sam").text()
                val list = main.select("ul.ar_rlos_bor li a")
                val volume = ComicVolumeBean()
                volume.title = ""
                val chapterList = arrayListOf<ComicChapterBean>()
                for (cpEle in list) {
                    val chapter = ComicChapterBean()
                    chapter.isVolume = false
                    chapter.chapterTitle = cpEle.text()
                    val url = cpEle.attr("href")
                    val chapterNum = url.split("/").last().replace(".html", "")
                    chapter.chapterId = chapterNum
                    chapter.url = url
                    chapterList.add(chapter)

                }
                volume.content = chapterList
                comicBean.chapters = arrayListOf(volume)
                comicBean.webKey = ComicSiteFactory.COMIC_XX
                comicBean
            }
            comic.postValue(comicDetailBean)
        }
    }

    override fun isReverse(): Boolean {
        return true
    }

    override fun setAuthorLink(context: Context, str: String): SpannableString {
        val ss = SpannableString(str)
        if (!str.contains("作者")) return ss
        val list = str.substring(3, str.length).trim().split("  ")
        var index = 3
        for (s in list) {
            ss.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ComicSearchActivity.startKeyword(context, s, 1)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

            }, index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(context,R.color.link_color)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //ss.setSpan(BackgroundColorSpan(resources.getColor(R.color.colorAccent)), index, index + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            index += s.length + 2

        }
        return ss
    }

}