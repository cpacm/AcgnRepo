package com.cpacm.comic.site.manhuadui

import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.comic.model.http.MhDuiRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *     漫画堆漫画数据列表
 * @author cpacm 2019-12-18
 */
class MhDuiListViewModel : ComicListViewModel() {

    var extra: String = ""
    var curPage = 1

    init {
    }

    override fun setType(type: ComicDataType, extra: String) {
        this.dataType = type
        this.extra = extra
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
        curPage = 1
        comicList.value?.clear()
        getUpdateList()
    }

    private fun getUpdateList() {
        var lastPage = 0
        withUI {
            val dataList = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.getMHDuiUpdateComic(curPage)
                val str = response.string()
                val body = ComicElement(str)

                val curPage = body.text("div.bottom_page > ul.pagination > li.active")?.toInt() ?: 1
                lastPage = body.attr("div.bottom_page > ul.pagination > li.last > a", "data-page")?.toInt()
                        ?: 0
                lastPage += 1
                val list = arrayListOf<ComicBean>()
                if (curPage <= lastPage) {
                    for (node in body.list("ul.list_con_li > li.list-comic")) {
                        list.add(parseNormalComic(node))
                    }
                }
                loadCategory(body)

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            title.postValue("最近更新")
            comicList.postValue(allList)
            hasMore.postValue(dataList.isNotEmpty() && curPage < lastPage)
            curPage += 1
        }
    }

    /**
     * 人气排行榜 ：popularity
     * 点击排行榜 ：click
     * 订阅排行榜 ：subscribe
     */
    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("日人气", "popularity-daily"))
        list.add(ComicMenuBean("日点击", "click-daily"))
        list.add(ComicMenuBean("日订阅", "subscribe-daily"))
        list.add(ComicMenuBean("魔法漫画", "mofa_popularity-daily"))
        list.add(ComicMenuBean("少年漫画", "shaonian_popularity-daily"))
        list.add(ComicMenuBean("少女漫画", "shaonv_popularity-daily"))
        list.add(ComicMenuBean("青年漫画", "qingnian_popularity-daily"))
        list.add(ComicMenuBean("搞笑漫画", "gaoxiao_popularity-daily"))
        list.add(ComicMenuBean("科幻漫画", "kehuan_popularity-daily"))
        list.add(ComicMenuBean("热血漫画", "rexue_popularity-daily"))
        list.add(ComicMenuBean("冒险漫画", "maoxian_popularity-daily"))
        list.add(ComicMenuBean("完结漫画", "wanjie_popularity-daily"))
        return list
    }

    private fun refreshRankList() {
        curPage = 1
        comicList.value?.clear()
        getRankList()
    }

    private fun getRankList() {
        withUI {
            val hhList = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.getMHDuiRankComic(extra)
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.wrap div.ph_r_con_li_c div.con_li_content")) {
                    list.add(parseRankComic(node))
                }
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(hhList)
            comicList.postValue(allList)
            hasMore.postValue(false)
            curPage += 1
        }
    }

    private fun refreshCategory() {
        curPage = 1
        comicList.value?.clear()
        getCategoryList()
    }

    private fun getCategoryList() {
        var lastPage = 0
        withUI {
            val dataList = withRequest {
                val response = MhDuiRetrofitHelper.instance.mhDuiApi.getMHDuiCategoryComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)

                val curPage = body.text("div.bottom_page > ul.pagination > li.active")?.toInt() ?: 1
                lastPage = body.attr("div.bottom_page > ul.pagination > li.last > a", "data-page")?.toInt()
                        ?: 0
                lastPage += 1
                val list = arrayListOf<ComicBean>()
                if (curPage <= lastPage) {
                    for (node in body.list("ul.list_con_li > li.list-comic")) {
                        list.add(parseNormalComic(node))
                    }
                }
                loadCategory(body)
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            comicList.postValue(allList)
            hasMore.postValue(dataList.isNotEmpty() && curPage < lastPage)
            curPage += 1
        }
    }

    private suspend fun loadCategory(body: ComicElement) {
        if (categoryList.isEmpty()) {
            withRequest {
                val nav = body.list("div.filter-nav > div.filter-item")
                for (node in nav[2].list("ul > li")) {
                    val title = node.get().select("a").text()
                    var extra = node.get().select("a").attr("href")
                    extra = extra.substring(1, extra.length - 1)
                    extra = extra.replace("riben-", "")
                    categoryList.add(ComicMenuBean(title ?: "未知", extra))
                }
                categoryList.removeAt(0)
            }
        }
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()

        comicBean.cover = node.get().select("a.comic_img img").attr("src")

        val div = node.get().select("span.comic_list_det")
        comicBean.title = div.select("h3 a").text()
        comicBean.author = div.select("p").first()?.text() ?: "未知"
        comicBean.category = div.select("p")[1]?.text()
        comicBean.status = div.select("p")[2]?.text()
        comicBean.description = div.select("p").last()?.text()
        comicBean.url = div.select("a").attr("href")

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

    private fun parseRankComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val a = node.get().select("a.dec_img").first()
        comicBean.url = a.attr("href")
        comicBean.cover = a.select("img").attr("src")
        val div = node.get().select("span.img_de")
        comicBean.title = div.select("h3 a").text().trim()
        val ul = div.select("ul")
        comicBean.author = ul.select("li").first().text()
        comicBean.status = ul.select("li")[1].text()
        comicBean.category = ul.select("li")[4].text()
        comicBean.description = div.select("p.com_about").text()
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

    override fun getLogo(): Int {
        return R.drawable.ic_comic_mhdui
    }
}