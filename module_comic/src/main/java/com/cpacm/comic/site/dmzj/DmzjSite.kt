package com.cpacm.comic.site.dmzj

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.model.http.DmzjApis
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_DMZJ
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class DmzjSite : ComicSiteFactory {

    override fun getOriginUri(): String {
        return DmzjApis.ORIGIN_URL
    }

    override fun getDetailUri(comicId: String): String {
        return DmzjApis.DETAIL_URL.replace("{id}", comicId)
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return DmzjListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return DmzjDetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return DmzjReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return DmzjSearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_dmzj, R.drawable.img_dmzj_cover,
            R.string.dmzj_title,
            COMIC_DMZJ
        )
    }
}