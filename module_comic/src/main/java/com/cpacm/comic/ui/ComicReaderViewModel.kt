package com.cpacm.comic.ui

import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.libarch.mvvm.BaseViewModel

/**
 * <p>
 *
 * @author cpacm 2019-12-12
 */
abstract class ComicReaderViewModel : BaseViewModel() {

    val chapterData = arrayListOf<ComicChapterBean>()
    var comicId = ""

    abstract fun getContentData(index: Int, order: Int, immediately: Boolean = false, offsetPage: Int = -1)


    fun loadData(index: Int, dir: Int, chapter: ComicContentBean, offsetPage: Int = -1) {

    }


}