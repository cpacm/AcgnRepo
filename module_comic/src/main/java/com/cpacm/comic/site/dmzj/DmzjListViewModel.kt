package com.cpacm.comic.site.dmzj

import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.comic.model.http.DmzjRetrofitHelper
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.libarch.model.http.RxUtils

/**
 * <p>
 *     动漫之家数据列表
 * @author cpacm 2019-12-06
 */
class DmzjListViewModel : ComicListViewModel() {

    var extra: Int = 0
    var curPage = 1

    init {
        loadCategory()
    }

    override fun setType(type: ComicDataType, extra: String) {
        this.dataType = type
        this.extra = extra.toIntOrNull()?:0
    }

    override fun refresh() {
        when (dataType) {
            ComicDataType.DATA_RECENTLY -> refreshUpdateList()
            ComicDataType.DATA_RANK -> refreshRankList()
            ComicDataType.DATA_CATEGORY -> refreshCategory()
        }

    }

    override fun loadMore() {
        when (dataType) {
            ComicDataType.DATA_RECENTLY -> getUpdateList()
            ComicDataType.DATA_RANK -> getRankList()
            ComicDataType.DATA_CATEGORY -> getCategoryList()
        }
    }

    private fun refreshUpdateList() {
        curPage = 0
        comicList.value?.clear()
        getUpdateList()
    }

    private fun getUpdateList() {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi.getDmzjUpdateComic(curPage)
                .compose(RxUtils.io_main())
                .subscribe({
                    val list = comicList.value ?: arrayListOf()
                    for (comic in it) {
                        list.add(comic.parse())
                    }
                    title.postValue("最近更新")
                    comicList.postValue(list)
                    hasMore.postValue(it.isNotEmpty())
                    curPage += 1
                }, {
                    error.postValue(it)
                })
        addDisposable(disposable)
    }

    private fun refreshRankList() {
        curPage = 0
        comicList.value?.clear()
        getRankList()
    }

    private fun getRankList() {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi.getDmzjRankComic(extra, curPage)
                .compose(RxUtils.io_main())
                .subscribe({
                    val list = comicList.value ?: arrayListOf()
                    for (comic in it) {
                        list.add(comic.parse())
                    }
                    comicList.postValue(list)
                    hasMore.postValue(it.isNotEmpty())
                    curPage += 1
                }, {
                    error.postValue(it)
                })
        addDisposable(disposable)
    }

    private fun refreshCategory() {
        curPage = 0
        comicList.value?.clear()
        getCategoryList()
    }

    private fun getCategoryList() {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi.getDmzjClassifyComic(extra, curPage)
                .compose(RxUtils.io_main())
                .subscribe({
                    val list = comicList.value ?: arrayListOf()
                    for (comic in it) {
                        list.add(comic.parse())
                    }
                    comicList.postValue(list)
                    hasMore.postValue(it.isNotEmpty())
                    curPage += 1
                }, {
                    error.postValue(it)
                })
        addDisposable(disposable)
    }

    private fun loadCategory() {
        val disposable = DmzjRetrofitHelper.instance.dmzjApi.getDmzjCategory()
                .compose(RxUtils.io_main())
                .subscribe({
                    categoryList.clear()
                    for (tag in it) {
                        categoryList.add(ComicMenuBean(tag.title ?: "未知", tag.tag_id.toString()))
                    }
                }, {
                    error.postValue(it)
                })
        addDisposable(disposable)
    }

    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("每日排行", "0"))
        list.add(ComicMenuBean("每周排行", "1"))
        list.add(ComicMenuBean("每月排行", "2"))
        list.add(ComicMenuBean("总排行", "3"))
        return list
    }

    override fun getLogo(): Int {
        return R.drawable.ic_comic_dmzj
    }
}