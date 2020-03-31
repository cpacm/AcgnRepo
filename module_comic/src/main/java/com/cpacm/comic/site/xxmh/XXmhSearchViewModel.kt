package com.cpacm.comic.site.xxmh

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.http.XXmhRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchViewModel
import com.cpacm.libarch.utils.MoeLogger
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.lang.Exception

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class XXmhSearchViewModel : ComicSearchViewModel() {
    override fun searchRequest(keyword: String, type: Int) {
        if (type == 0) {
            searchNormal(keyword)
        } else if (type == 1) {
            searchWord(keyword)
        }
    }

    fun searchNormal(keyword: String) {
        if (this.page == 0) this.page = 1//页数从1开始
        var lastPage = 0
        withUI {
            val comicList = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.xxSearch(keyword, page)
                val data = Jsoup.parse(response.string())
                val elements: Elements = data.select("div#main div.ar_list_co ul dl")
                val list = arrayListOf<ComicBean>()
                for (e in elements) {
                    val comicBean = ComicBean()
                    val div = e.select("dd")
                    comicBean.cover = e.select("dt > a > img").attr("src")
                    comicBean.url = div.select("h1 a").attr("href")
                    comicBean.status = div.select("i.author a").first().text()
                    comicBean.title = div.select("h1 a").text()
                    comicBean.category = div.select("i.status span")?.text()
                    comicBean.author = div.select("i.author a").last()?.text() ?: "未知"

                    comicBean.description = div.select("i.info")?.text() ?: "暂未描述"
                    comicBean.webKey = ComicSiteFactory.COMIC_XX
                    comicBean.website = "新新漫画"
                    //链接样式为 http://ddmmcc.com/comic/1839741 => [http://ddmmcc.com/comic/][/18][id]  前面部分是固定的
                    if (comicBean.url != null) {
                        val arr = comicBean.url!!.split("_")
                        var last = arr.last().replace(".html", "")
                        comicBean.id = last
                    }
                    list.add(comicBean)
                }

                val pages = data.select("div#main div.pages_s span")
                try {
                    lastPage = pages.text().split("/").last().toInt()
                } catch (e: Exception) {
                    MoeLogger.e(e.toString())
                }

                list
            }
            val allList = results.value ?: arrayListOf()
            allList.addAll(comicList)
            results.postValue(allList)
            hasMore.postValue(page < lastPage)
            page += 1

        }
    }

    fun searchWord(keyword: String) {
        page = 1
        var lastPage = 0
        withUI {
            val comicList = withRequest {
                val response = XXmhRetrofitHelper.instance.xxmhApis.xxSearchWord(keyword, page)
                val data = Jsoup.parse(response.string())
                val elements: Elements = data.select("div#main div.ar_list_co ul dl")
                val list = arrayListOf<ComicBean>()
                for (e in elements) {
                    val comicBean = ComicBean()
                    val div = e.select("dd")
                    comicBean.cover = e.select("dt > a > img").attr("src")
                    comicBean.url = div.select("h1 a").attr("href")
                    comicBean.status = div.select("i.author a").first().text()
                    comicBean.title = div.select("h1 a").text()
                    comicBean.category = div.select("i.status span")?.text()
                    comicBean.author = div.select("i.author a").last()?.text() ?: "未知"

                    comicBean.description = div.select("i.info")?.text() ?: "暂未描述"
                    comicBean.webKey = ComicSiteFactory.COMIC_XX
                    comicBean.website = "新新漫画"
                    //链接样式为 http://ddmmcc.com/comic/1839741 => [http://ddmmcc.com/comic/][/18][id]  前面部分是固定的
                    if (comicBean.url != null) {
                        val arr = comicBean.url!!.split("_")
                        var last = arr.last().replace(".html", "")
                        comicBean.id = last
                    }
                    list.add(comicBean)
                }

                val pages = data.select("div#main div.pages_s span")
                try {
                    lastPage = pages.text().split("/").last().toInt()
                } catch (e: Exception) {
                    MoeLogger.e(e.toString())
                }

                list
            }
            results.postValue(comicList)
            hasMore.postValue(page < lastPage)
            page += 1

        }
    }

}