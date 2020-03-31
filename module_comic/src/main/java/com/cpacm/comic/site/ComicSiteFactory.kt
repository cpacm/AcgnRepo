package com.cpacm.comic.site

import com.cpacm.comic.model.bean.ComicSiteBean
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.comic.ui.site.ComicListViewModel

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
interface ComicSiteFactory {
    fun getOriginUri(): String

    fun getDetailUri(comicId: String): String

    fun getVMListClass(): Class<out ComicListViewModel>

    fun getVMDetailClass(): Class<out ComicDetailViewModel>

    fun getVMContentClass(): Class<out ComicReaderViewModel>

    fun getVMSearchClass(): Class<out ComicSearchViewModel>

    fun getComicSiteBean(): ComicSiteBean

    companion object {

        fun getComicSite(className: Class<out ComicSiteFactory>): ComicSiteFactory? {
            return className.getDeclaredConstructor().newInstance()
        }

        const val COMIC_DMZJ = "dmzj"
        const val COMIC_HH = "hh"//汗汗漫画
        const val COMIC_MHDUI = "mhdui"//漫画堆
        const val COMIC_PICA = "pica"//哔咔
        const val COMIC_XX = "xxmh"//新新漫画
        const val COMIC_MH123 = "mh123"//漫画123
        const val COMIC_MGBZ = "mgbz"//mangabz
    }

}