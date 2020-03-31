package com.cpacm.comic.site.mh123

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.http.Mh123Apis
import com.cpacm.comic.model.http.Mh123RetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.utils.ComicElement

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class Mh123SearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String, type: Int) {
        if (page == 0) page = 1//页数从1开始
        withUI {
            var lastPage = 1
            val comicList = withRequest {
                val response = Mh123RetrofitHelper.instance.mh123Apis.searchWord(getKeyWord(page, keyword))
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
            val allList = results.value ?: arrayListOf()
            allList.addAll(comicList)
            results.postValue(allList)
            hasMore.postValue(comicList.isNotEmpty() && page < lastPage)
            page += 1
        }
    }

    private fun getKeyWord(page: Int, keyword: String): String {
        var word = "vod-search"
        if (page == 1) {
            word += "-wd-$keyword"
        } else {
            word += "-pg-${page}-wd-$keyword"
        }
        return word
    }

    private fun parseNormalComic(node: ComicElement): ComicBean {
        val comicBean = ComicBean()
        val div = node.get()
        comicBean.cover = div.select("a img").attr("data-src")
        comicBean.title = div.select("a p").text()
        comicBean.author = "未知"
        comicBean.status = div.select("a span").text()
        comicBean.category = div.select("a em").text()
        comicBean.description = ""
        comicBean.url = Mh123Apis.BASE_URL + div.select("a").attr("href")

        comicBean.webKey = ComicSiteFactory.COMIC_MH123
        comicBean.website = "漫画123"
        if (comicBean.url != null) {
            val arr = comicBean.url!!.split("/")
            var last = arr.last().replace(".html", "")
            comicBean.id = last
        }
        return comicBean
    }


}