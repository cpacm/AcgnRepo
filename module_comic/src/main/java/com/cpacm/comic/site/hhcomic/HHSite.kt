package com.cpacm.comic.site.hhcomic

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.model.http.HHApis
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_HH
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class HHSite : ComicSiteFactory {

    override fun getOriginUri(): String {
        return HHApis.ORIGIN_URL
    }

    override fun getDetailUri(comicId: String): String {
        return HHApis.DETAIL_URL.replace("{id}", comicId)
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return HHListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return HHDetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return HHReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return HHSearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_hhlogo, R.drawable.img_hh_cover,
            R.string.hh_title,
            COMIC_HH
        )
    }
}