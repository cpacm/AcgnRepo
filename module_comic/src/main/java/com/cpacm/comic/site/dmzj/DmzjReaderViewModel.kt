package com.cpacm.comic.site.dmzj

import com.cpacm.comic.model.http.DmzjRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel
import com.cpacm.libarch.model.http.ApiException
import com.cpacm.libarch.model.http.RxUtils
import io.reactivex.disposables.Disposable

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class DmzjReaderViewModel : ComicReaderViewModel() {

    var loadDisposable: Disposable? = null

    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        loadDisposable?.dispose()
        val chapterBean = chapterData[index]
        loadDisposable = DmzjRetrofitHelper.instance.dmzjApi
                .getDmzjComicContent(comicId, chapterBean.chapterId ?: "")
                .compose(RxUtils.io_main())
                .subscribe({
                    if (it.page_url.isNullOrEmpty()) {
                        error.postValue(ApiException("漫画数据为空"))
                    } else {
                        loadData(index, order, it.parse(), offsetPage)
                    }
                }, { error.postValue(it) })

        addDisposable(loadDisposable!!)
    }
}