package com.cpacm.comic.ui.search

import androidx.lifecycle.MutableLiveData
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.libarch.mvvm.BaseViewModel

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
abstract class ComicSearchViewModel : BaseViewModel() {
    val results = MutableLiveData<MutableList<ComicBean>>()

    open var page = 0
    val hasMore = MutableLiveData<Boolean>()
    var keyword = MutableLiveData<String>()
    var type: Int = 0

    open fun startSearch(keyword: String, type: Int = 0) {
        this.keyword.value = keyword
        this.type = type
        search()
    }


    fun search() {
        page = 0
        results.value?.clear()
        searchRequest(keyword.value ?: "unknown", type)
    }

    fun searchMore() {
        searchRequest(keyword.value ?: "unknown", type)
    }

    abstract fun searchRequest(keyword: String, type: Int = 0)

}