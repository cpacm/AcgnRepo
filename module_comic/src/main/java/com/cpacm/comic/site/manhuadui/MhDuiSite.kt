package com.cpacm.comic.site.manhuadui

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.model.http.MhDuiApis
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_MHDUI
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class MhDuiSite : ComicSiteFactory {

    override fun getOriginUri(): String {
        return MhDuiApis.ORIGIN_URL
    }

    override fun getDetailUri(comicId: String): String {
        return MhDuiApis.DETAIL_URL.replace("{id}", comicId)
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return MhDuiListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return MhDuiDetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return  MhDuiReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return  MhDuiSearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_mhdui, R.drawable.img_mhd_cover,
            R.string.mhd_title,
            COMIC_MHDUI
        )
    }
}