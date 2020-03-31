package com.cpacm.comic.site.hhcomic

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.http.HHRetrofitHelper
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.ui.search.ComicSearchViewModel
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class HHSearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String,type:Int) {
        withUI {
           val comicList =  withRequest {
                val response = HHRetrofitHelper.instance.hhApi.hhSearch(keyword)
                val data = Jsoup.parse(response.string())
                val elements: Elements = data.select("ul.se-list li")
                val list = arrayListOf<ComicBean>()
                for (e in elements) {
                    val comicBean = ComicBean()
                    val div = e.select("div.con")
                    comicBean.cover = e.select("a > img").attr("src")
                    comicBean.url = e.select("a").first().attr("href")
                    comicBean.status = e.select("a > span.h").text()
                    comicBean.title = div.select("h3").text()
                    comicBean.category = div.select("p")[1]?.text()
                    comicBean.author = div.select("p").first()?.text() ?: "未知"

                    val date = div.select("p > span")?.text() ?: "最近"
                    comicBean.description = "最近更新：$date"
                    comicBean.webKey = ComicSiteFactory.COMIC_HH
                    comicBean.website = "汗汗漫画"
                    //链接样式为 http://ddmmcc.com/comic/1839741 => [http://ddmmcc.com/comic/][/18][id]  前面部分是固定的
                    if (comicBean.url != null) {
                        comicBean.id = (comicBean.url!!.split("/18")[1])
                    }
                    list.add(comicBean)
                }
                list
            }
            results.postValue(comicList)
            hasMore.postValue(false)

        }
    }

}