package com.cpacm.comic.site.manhuadui

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.http.MhDuiRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.utils.ComicElement

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class MhDuiSearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String, type: Int) {
        if (this.page == 0) this.page = 1//页数从1开始
        withUI {
            var lastPage = 0
            val comicList = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.mhDuiSearch(keyword, page)
                val str = response.string()
                val body = ComicElement(str)
                var curPage = body.attr("div.bottom_page > ul.pagination > li.active > a", "data-page")?.toInt()
                        ?: 0
                lastPage = body.attr("div.bottom_page > ul.pagination > li.last > a", "data-page")?.toInt()
                        ?: curPage
                curPage += 1
                lastPage += 1
                val list = arrayListOf<ComicBean>()
                if (curPage <= lastPage) {
                    for (node in body.list("ul.list_con_li > li.list-comic")) {
                        list.add(parseNormalComic(node))
                    }
                }
                list
            }
            val allList = results.value ?: arrayListOf()
            allList.addAll(comicList)
            results.postValue(allList)
            hasMore.postValue(comicList.isNotEmpty() && page < lastPage)
            page += 1
        }

    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()

        comicBean.cover = node.get().select("a.image-link img").attr("src")
        val div = node.get()
        comicBean.title = div.select("p a").text()
        comicBean.url = div.select("p a").attr("href")
        comicBean.author = div.select("p.auth").text() ?: "未知"
        comicBean.category = ""
        comicBean.status = div.select("p.newPage")?.text()
        comicBean.description = "最新内容：${comicBean.status}"

        comicBean.webKey = ComicSiteFactory.COMIC_MHDUI
        comicBean.website = "漫画堆"
        //链接样式为 http://ddmmcc.com/comic/1839741 => [http://ddmmcc.com/comic/][/18][id]  前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("/")
            var last = arr.last()
            if (last.isEmpty()) {
                val size = arr.size
                if (size - 2 >= 0) {
                    last = arr.get(size - 2)
                }
            }
            comicBean.id = last
        }
        return comicBean
    }

}