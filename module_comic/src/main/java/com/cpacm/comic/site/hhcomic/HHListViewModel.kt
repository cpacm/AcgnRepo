package com.cpacm.comic.site.hhcomic

import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.comic.model.http.HHApis
import com.cpacm.comic.model.http.HHRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.comic.utils.ComicElement

/**
 * <p>
 *     汗汗漫画数据列表
 * @author cpacm 2019-12-18
 */
class HHListViewModel : ComicListViewModel() {

    var extra: String = ""
    var curPage = 1


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
            val hhList = withRequest {
                val response = HHRetrofitHelper.instance.hhApi.getHHUpdateComic()
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div#list div.cTopComicList > div.cComicItem")) {
                    list.add(parseUpdateComic(node))
                }

                loadCategory(body)

                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(hhList)
            title.postValue("最近更新")
            comicList.postValue(allList)
            hasMore.postValue(false)
            curPage = curPage + 1
        }
    }

    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("最多人看", "hotrating"))
        list.add(ComicMenuBean("评分最高", "toprating"))
        list.add(ComicMenuBean("评论最多", "hoorating"))
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
                val response = HHRetrofitHelper.instance.hhApi.getHHRankComic(extra)
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div#list div.cTopComicList > div.cComicItem")) {
                    list.add(parseUpdateComic(node))
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
        withUI {
            val hhList = withRequest {
                val response = HHRetrofitHelper.instance.hhApi.getHHCategoryComic(extra, curPage)
                val str = response.string()
                val body = ComicElement(str)
                val list = arrayListOf<ComicBean>()
                for (node in body.list("div#list div.cComicList li")) {
                    list.add(parseNormalComic(node))
                }
                list
            }
            val allList = comicList.value ?: arrayListOf()
            allList.addAll(hhList)
            comicList.postValue(allList)
            hasMore.postValue(hhList.isNotEmpty())
            curPage = curPage + 1
        }
    }

    private fun loadCategory(node: ComicElement) {
        withUI {
            withRequest {
                val nav = node.list("div.cHNav > div > span")
                // http://hhimm.com/comic/class_1.html
                categoryList.clear()
                for (e in nav) {
                    val title = e.get().select("a").text()
                    val url = e.get().select("a").attr("href")
                    val extra = url.split("_").last().split(".").first()
                    categoryList.add(ComicMenuBean(title, extra))
                }
            }
        }
    }

    private fun parseUpdateComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val div = node.get()
        comicBean.cover = div.select("div.cListSlt > a > img").attr("src")
        comicBean.url = HHApis.BASE_URL2 + div.select("div.cListSlt > a").attr("href")
        comicBean.title = div.select("a > span.cComicTitle").text()
        comicBean.author = div.select("span.cComicAuthor").text()
        comicBean.category = div.select("span.cComicRating").text()
        comicBean.description = div.select("div.cComicMemo").text()
        comicBean.webKey = ComicSiteFactory.COMIC_HH
        comicBean.website = "汗汗酷漫"
        if (comicBean.url != null) {
            //http://www.hhimm.com/manhua/39961.html
            comicBean.id = comicBean.url!!.split("/").last().split(".").first()
        }
        return comicBean
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val a = node.get().select("a")
        comicBean.url = HHApis.BASE_URL2+a.attr("href")
        comicBean.cover = a.select("img").attr("src")
        comicBean.title = a.attr("title")

        comicBean.description = "暂无描述"
        comicBean.webKey = ComicSiteFactory.COMIC_HH
        comicBean.website = "汗汗酷漫"
        //链接样式为 http://ddmmcc.com/comic/1839741 => [http://ddmmcc.com/comic/][/18][id]  前面部分是固定的
        if (comicBean.url != null) {
            //http://www.hhimm.com/manhua/39961.html
            comicBean.id = comicBean.url!!.split("/").last().split(".").first()
        }
        return comicBean
    }

    override fun getLogo(): Int {
        return R.drawable.ic_comic_hhlogo
    }
}