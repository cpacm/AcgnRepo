package com.cpacm.comic.site.dmzj

import com.cpacm.comic.model.http.DmzjRetrofitHelper
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.libarch.model.http.RxUtils

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class DmzjSearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String, type: Int) {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi
                .dmzjSearch(keyword, page)
                .compose(RxUtils.io_main())
                .subscribe({
                    val list = results.value ?: arrayListOf()
                    for (dmzjBean in it) {
                        list.add(dmzjBean.parse())
                    }
                    results.postValue(list)
                    if (!it.isNullOrEmpty() && it.size >= 10) {
                        hasMore.postValue(true)
                    } else {
                        hasMore.postValue(false)
                    }
                    this.page += 1
                }, {
                    error.postValue(it)
                })
        addDisposable(disposable)
    }

}