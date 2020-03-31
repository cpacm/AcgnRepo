package com.cpacm.comic.site.dmzj

import com.cpacm.comic.model.http.DmzjRetrofitHelper
import com.cpacm.comic.ui.site.ComicDetailViewModel
import com.cpacm.libarch.model.http.RxUtils

/**
 * <p>
 *     动漫之家详情页
 * @author cpacm 2019-12-06
 */
class DmzjDetailViewModel : ComicDetailViewModel() {

    override fun getComicDetail(comicId: String) {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi
                .getDmzjComic(comicId)
                .compose(RxUtils.io_main())
                .subscribe({
                    comic.postValue(it.parse())
                }, { error.postValue(it) })

        addDisposable(disposable)
    }

    override fun isReverse(): Boolean {
        return true
    }

}