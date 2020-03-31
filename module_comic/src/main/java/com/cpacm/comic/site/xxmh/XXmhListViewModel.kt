package com.cpacm.comic.site.xxmh

import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.comic.model.http.*
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.comic.utils.ComicElement
import com.cpacm.libarch.utils.MoeLogger
import java.lang.Exception

/**
 * <p>
 *     新新漫画漫画数据列表
 * @author cpacm 2019-12-18
 */
class XXmhListViewModel : ComicListViewModel() {

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
        withUI {
            val dataList = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.getRecommend()
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div#main div.conmmand_comic_co  li")) {
                    list.add(parseNormalComic(node))
                }

                loadCategory(body)

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            hasMore.postValue(false)
            title.postValue("最近更新")
            comicList.postValue(allList)
        }
    }

    /**
     * 人气排行榜 ：popularity
     * 点击排行榜 ：click
     * 订阅排行榜 ：subscribe
     */
    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("完结漫画", "wanjie/index.html"))
        list.add(ComicMenuBean("连载漫画", "lianzai/index.html"))
        list.add(ComicMenuBean("最新漫画", "new_coc.html"))
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
                val response = XXmhRetrofitHelper.instance.xxmhApis.getXXRankComic(extra)
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div#main div.ar_list_co li")) {
                    list.add(parseRankComic(node))
                }
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(hhList)
            hasMore.postValue(false)
            comicList.postValue(allList)
        }
    }

    private fun refreshCategory() {
        curPage = 0
        comicList.value?.clear()
        getCategoryList()
    }

    private fun getCategoryList() {
        var lastPage = 0
        withUI {
            val dataList = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.getXXCategoryComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)

                val curPage = (body.text("div#main div.pages_s > span > b")?.toInt() ?: 1) - 1
                try {
                    lastPage = body.text("div#main div.pages_s > span").split("/").last().toInt()
                } catch (e: Exception) {
                    MoeLogger.e(e.toString())
                }
                val list = arrayListOf<ComicBean>()
                if (curPage <= lastPage) {
                    for (node in body.list("div#main div.ar_list_co dl")) {
                        list.add(parseCategoryComic(node))
                    }
                }
                loadCategory(body)
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            hasMore.postValue(dataList.isNotEmpty() && curPage < lastPage - 1)
            comicList.postValue(allList)
            curPage += 1
        }
    }

    private suspend fun loadCategory(body: ComicElement) {
        if (categoryList.isEmpty()) {
            withRequest {
                val nav = body.list("div#nav ul li")
                for (node in nav) {
                    val title = node.get().select("a").text()
                    var extra = node.get().select("a").attr("href")
                    if (extra == "/") continue
                    extra = extra.replace("/", "").replace("index.html", "")
                    categoryList.add(ComicMenuBean(title ?: "未知", extra))
                }
                categoryList
            }
        }
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val div = node.get()
        comicBean.cover = div.select("a img").attr("src")
        comicBean.title = div.select("i").text()
        comicBean.author = ""
        comicBean.category = ""
        comicBean.status = ""
        comicBean.description = ""
        comicBean.url = XXmhApis.BASE_URL + div.select("a").attr("href")

        comicBean.webKey = ComicSiteFactory.COMIC_XX
        comicBean.website = "新新漫画"
        //链接样式为 https://www.177mh.net/colist_244253.html   前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("_")
            var last = arr.last().replace(".html", "")
            comicBean.id = last
        }
        return comicBean
    }

    private fun parseRankComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()

        val div = node.get()
        comicBean.url = XXmhApis.BASE_URL + div.select("a").attr("href")
        comicBean.cover = div.select("a img").attr("src")
        comicBean.title = div.select("span a").text().trim()
        comicBean.author = "未知"
        comicBean.category = ""
        comicBean.status = ""
        comicBean.description = ""
        comicBean.webKey = ComicSiteFactory.COMIC_XX
        comicBean.website = "新新漫画"
        //链接样式为 https://www.177mh.net/colist_244253.html   前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("_")
            var last = arr.last().replace(".html", "")
            comicBean.id = last
        }
        return comicBean
    }

    private fun parseCategoryComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()

        val div = node.get()
        comicBean.url = XXmhApis.BASE_URL + div.select("dt > a").attr("href")
        comicBean.cover = div.select("dt > a > img").attr("src")
        comicBean.title = div.select("dd > h1 > a").text().trim()
        comicBean.author = "作者：" + div.select("dd > i.author > a").text()
        comicBean.category = ""
        comicBean.status = "状态：" + div.select("dd > i.status > a").text()
        comicBean.description = div.select("dd > i.info").text() ?: ""
        comicBean.webKey = ComicSiteFactory.COMIC_XX
        comicBean.website = "新新漫画"
        //链接样式为 https://www.177mh.net/colist_244253.html   前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("_")
            var last = arr.last().replace(".html", "")
            comicBean.id = last
        }
        return comicBean
    }

    override fun getLogo(): Int {
        return R.drawable.ic_comic_xx
    }
}