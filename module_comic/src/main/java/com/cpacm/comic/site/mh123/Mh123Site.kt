package com.cpacm.comic.site.mh123

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.model.http.Mh123Apis
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.ComicSiteFactory.Companion.COMIC_MH123
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class Mh123Site : ComicSiteFactory {

    override fun getOriginUri(): String {
        return Mh123Apis.ORIGIN_URL
    }

    override fun getDetailUri(comicId: String): String {
        return Mh123Apis.DETAIL_URL.replace("{id}", comicId)
    }

    override fun getVMListClass(): Class<out ComicListViewModel> {
        return Mh123ListViewModel::class.java
    }

    override fun getVMDetailClass(): Class<out ComicDetailViewModel> {
        return Mh123DetailViewModel::class.java
    }

    override fun getVMContentClass(): Class<out ComicReaderViewModel> {
        return Mh123ReaderViewModel::class.java
    }

    override fun getVMSearchClass(): Class<out ComicSearchViewModel> {
        return Mh123SearchViewModel::class.java
    }

    override fun getComicSiteBean(): ComicSiteBean {
        return ComicSiteBean(
            R.drawable.ic_comic_mh123, R.drawable.img_mh123_cover,
            R.string.mh123_title,
            COMIC_MH123
        )
    }
}