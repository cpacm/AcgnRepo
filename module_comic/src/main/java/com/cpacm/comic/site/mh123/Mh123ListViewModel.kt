package com.cpacm.comic.site.mh123

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
class Mh123ListViewModel : ComicListViewModel() {

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
                val response = Mh123RetrofitHelper.instance.mh123Apis.getUpdateComic(curPage)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.content div.box04 ul#list_img li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.content div.box04 div.pages em.num").text()
                lastPage = pageStr.split("/").last().toInt()

                loadCategory()

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
        list.add(ComicMenuBean("排行榜", "page/hot"))
        list.add(ComicMenuBean("少年热血", "list/shaonianrexue"))
        list.add(ComicMenuBean("武侠格斗", "list/wuxiagedou"))
        list.add(ComicMenuBean("恐怖灵异", "list/kongbulingyi"))
        list.add(ComicMenuBean("耽美人生", "list/danmeirensheng"))
        list.add(ComicMenuBean("少女爱情", "list/shaonvaiqing"))
        list.add(ComicMenuBean("国产漫画", "page/guochan"))
        list.add(ComicMenuBean("日韩漫画", "page/rihan"))
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
                val response = Mh123RetrofitHelper.instance.mh123Apis.getRankComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.content div.box04 ul#list_img li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.content div.box04 div.pages em.num").text()
                lastPage = pageStr.split("/").last().toInt()
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
                val response = Mh123RetrofitHelper.instance.mh123Apis.getCategoryComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.content div.box04 ul#list_img li")) {
                    list.add(parseNormalComic(node))
                }
                val pageStr = body.get().select("div.content div.box04 div.pages em.num").text()
                lastPage = pageStr.split("/").last().toInt()
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(dataList)
            comicList.postValue(allList)
            hasMore.postValue(curPage < lastPage)
            curPage += 1
        }
    }

    private suspend fun loadCategory() {
        if (categoryList.isEmpty()) {
            withRequest {
                val response = Mh123RetrofitHelper.instance.mh123Apis.loadCategoryComic()
                val str = response.string()
                val body = ComicElement(str)

                val nav = body.list("div.content div.warp dl#ticai dd")
                for (node in nav) {
                    val title = node.get().select("a").text()
                    var extra = node.get().select("a").attr("href")
                    val ls = extra.split("/")
                    if (ls.size >= 3) {
                        extra = ls[2]
                    }
                    categoryList.add(ComicMenuBean(title ?: "未知", extra))
                }
                categoryList
            }
        }
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val div = node.get()
        comicBean.cover = div.select("a img").attr("data-src")
        comicBean.title = div.select("a p").text()
        comicBean.author = ""
        comicBean.status = ""
        comicBean.category = div.select("a em").text()
        comicBean.description = div.select("a span").text()
        comicBean.url = Mh123Apis.BASE_URL + div.select("a").attr("href")

        comicBean.webKey = ComicSiteFactory.COMIC_MH123
        comicBean.website = "漫画123"
        //链接样式为 https://www.manhua123.net/comic/123.html  前面部分是固定的
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("/")
            var last = arr.last().replace(".html", "")
            comicBean.id = last
        }
        return comicBean
    }


    override fun getLogo(): Int {
        return R.drawable.ic_comic_mh123
    }
}