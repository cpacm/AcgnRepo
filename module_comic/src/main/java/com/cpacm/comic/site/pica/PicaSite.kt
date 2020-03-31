package com.cpacm.comic.site.pica

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_PICA
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class PicaSite : ComicSiteFactory {

    override fun getOriginUri(): String {
        return ""
    }

    override fun getDetailUri(comicId: String): String {
        return ""
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return PicaListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return PicaDetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return PicaReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return PicaSearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_pica, R.drawable.img_pica_cover,
            R.string.pica_title,
            COMIC_PICA
        )
    }
}