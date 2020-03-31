package com.cpacm.comic.site.pica

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.cpacm.comic.R
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.http.PicaRetrofitHelper
import com.cpacm.comic.ui.search.ComicSearchActivity
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.libarch.model.http.ApiException

/**
 * <p>
 *     详情页
 * @author cpacm 2019-12-26
 */
class PicaDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        withUI {
            val comicDetail = withRequest {
                val response = PicaRetrofitHelper.instance.picaApi.getComicDetail(comicId)
                if (response.code == 200) {
                    val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
                    val comicDetail = response.data!!.comic
                    comicDetail.parse(imageServer)
                } else {
                    ComicDetailBean()
                }
            }
            if (comicDetail.id.isEmpty()) {
                error.postValue(ApiException("获取数据失败"))
                return@withUI
            }
            val volumeBean = ComicVolumeBean()
            val list = withRequest {
                val chapterList = arrayListOf<ComicChapterBean>()
                var epLoop = true
                var page = 1
                while (epLoop) {
                    val response = PicaRetrofitHelper.instance.picaApi.getComicEp(comicDetail.id, page)
                    if (response.code == 200) {
                        val doc = response.data!!.eps
                        epLoop = doc.page < doc.pages
                        page += 1
                        for (ep in doc.docs) {
                            chapterList.add(ep.parse())
                        }
                    } else {
                        epLoop = false
                    }
                }
                chapterList
            }
            volumeBean.content = list
            val volumeList = arrayListOf(volumeBean)
            comicDetail.chapters = volumeList
            comic.postValue(comicDetail)
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
                    ComicSearchActivity.startKeyword(context, s, 3)
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

    override fun setCategoryLink(context: Context, str: String): SpannableString {
        val ss = SpannableString(str)
        if (str.contains("未分类")) return ss
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