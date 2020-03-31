package com.cpacm.comic.site.xxmh

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.model.http.XXmhApis
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_XX
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class XXmhSite : ComicSiteFactory {

    override fun getOriginUri(): String {
        return XXmhApis.ORIGIN_URL
    }

    override fun getDetailUri(comicId: String): String {
        return XXmhApis.DETAIL_URL.replace("{id}", comicId)
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return  XXmhListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return XXmhDetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return XXmhReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return XXmhSearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_xx, R.drawable.img_xx_cover,
            R.string.xx_title,
            COMIC_XX
        )
    }
}