package com.cpacm.comic.site.mgbz

import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.comic.model.http.*
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *     漫画数据列表
 * @author cpacm 2019-12-18
 */
class MgbzListViewModel : ComicListViewModel() {

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
        var lastPage = 1
        withUI {
            val dataList = withRequest {
                val response = MgbzRetrofitHelper.instance.mgbzApis.getUpdateComic(curPage)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.container ul.mh-list li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.container div.page-pagination ul li").last().select("a").attr("data-index").trim()
                lastPage = pageStr.toInt()

                loadCategory(body)

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            title.postValue("最近更新")
            comicList.postValue(allList)
            hasMore.postValue(curPage < lastPage)
            curPage++
        }
    }

    /**
     * 人气排行榜 ：popularity
     * 点击排行榜 ：click
     * 订阅排行榜 ：subscribe
     */
    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("人气", ""))
        list.add(ComicMenuBean("连载中", "-0-1-10"))
        list.add(ComicMenuBean("已完结", "-0-2-10"))
        return list
    }

    private fun refreshRankList() {
        curPage = 1
        comicList.value?.clear()
        getRankList()
    }

    private fun getRankList() {
        var lastPage = 1
        withUI {
            val hhList = withRequest {
                val response = MgbzRetrofitHelper.instance.mgbzApis.getRankComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.container ul.mh-list li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.container div.page-pagination ul li")
                lastPage = pageStr.last().select("a").attr("data-index").trim().toInt()

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(hhList)
            comicList.postValue(allList)
            hasMore.postValue(curPage < lastPage)
            curPage++
        }
    }

    private fun refreshCategory() {
        curPage = 1
        comicList.value?.clear()
        getCategoryList()
    }

    private fun getCategoryList() {
        var lastPage = 1
        withUI {
            val dataList = withRequest {
                val response = MgbzRetrofitHelper.instance.mgbzApis.getCategoryComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.container ul.mh-list li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.container div.page-pagination ul li")
                lastPage = pageStr.last().select("a").attr("data-index").trim().toInt()

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            comicList.postValue(allList)
            hasMore.postValue(curPage < lastPage)
            curPage += 1
        }
    }

    private suspend fun loadCategory(body: ComicElement) {
        if (categoryList.isEmpty()) {
            withRequest {
                val nav = body.list("div.container div.class-line")[0].get().select("a")
                for (node in nav) {
                    val title = node.text()
                    var extra = node.attr("href")
                    val ls = extra.replace("manga-list", "").replace("/", "")
                    if (ls.startsWith("-")) {
                        extra = ls.substring(1, ls.length)
                    } else {
                        extra = ls
                    }

                    categoryList.add(ComicMenuBean(title ?: "未知", extra))
                }
                categoryList
            }
        }
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val div = node.get().select("div.mh-item")
        comicBean.cover = div.select("a img.mh-cover").attr("src")
        comicBean.title = div.select("h2.title a").text()
        comicBean.author = ""
        comicBean.status = ""
        comicBean.category = ""
        comicBean.description = div.select("p.chapter span").text() + "：" + div.select("p.chapter a").text()
        comicBean.url = MgbzApis.BASE_URL2 + div.select("a").attr("href")

        comicBean.webKey = ComicSiteFactory.COMIC_MGBZ
        comicBean.website = "Mangabz"
        //链接样式为 http://www.mangabz.com/1363bz/  前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("/")
            val last = arr[arr.size - 2]
            comicBean.id = last
        }
        return comicBean
    }


    override fun getLogo(): Int {
        return R.drawable.ic_comic_mgbz
    }
}