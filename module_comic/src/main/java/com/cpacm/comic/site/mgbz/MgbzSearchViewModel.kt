package com.cpacm.comic.site.mgbz

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.http.*
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.comic.utils.ComicElement

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class MgbzSearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String, type: Int) {
        if (page == 0) page = 1//页数从1开始
        withUI {
            var lastPage = 1
            val comicList = withRequest {
                val response = MgbzRetrofitHelper.instance.mgbzApis.search(keyword, page)
                val str = response.string()
                val body = ComicElement(str)

                val list = arrayListOf<ComicBean>()
                for (node in body.list("div.container ul.mh-list li")) {
                    list.add(parseNormalComic(node))
                }

                val pageStr = body.get().select("div.container div.page-pagination ul li").last().select("a").attr("href")
                lastPage = pageStr.split("=").last().toInt()

                list
            }
            val allList = results.value ?: arrayListOf()
            allList.addAll(comicList)
            results.postValue(allList)
            hasMore.postValue(page < lastPage)
            page += 1
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


}