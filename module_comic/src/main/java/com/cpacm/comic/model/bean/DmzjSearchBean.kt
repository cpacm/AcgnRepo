package com.cpacm.comic.model.bean

import com.cpacm.comic.ComicApp
import com.cpacm.comic.site.ComicSiteFactory

/**
 *
 *
 *
 * @author cpacm 2019-12-03
 */
class DmzjSearchBean {


    /**
     * alias_name :
     * authors : 安彦良和
     * copyright : 0
     * cover : https://images.dmzj.com/webpic/17/snzd.jpg
     * device_show : 7
     * grade : 0
     * hidden : 0
     * hot_hits : 21110
     * last_name : 第03卷完
     * quality : 1
     * status : 0
     * title : 圣女贞德
     * types : 冒险/历史/高清单行
     * id : 5341
     */

    var alias_name: String? = null
    var authors: String? = null
    var copyright: Int = 0
    var cover: String? = null
    var device_show: Int = 0
    var grade: Int = 0
    var hidden: Int = 0
    var hot_hits: Int = 0
    var last_name: String? = null
    var quality: Int = 0
    var status: Int = 0
    var title: String? = null
    var types: String? = null
    var id: Int = 0

    fun parse(): ComicBean {
        val comic = ComicBean()
        comic.id = id.toString()
        comic.title = title
        comic.status = "热度：${hot_hits}"
        comic.category = types
        comic.cover = cover
        comic.author = authors ?: "未知"
        var des = ""
        if (!last_name.isNullOrEmpty()) {
            des += "最新章节:${last_name}"
        }
        comic.description = des
        comic.website = "动漫之家"
        comic.webKey = ComicSiteFactory.COMIC_DMZJ
        comic.url = "https://m.dmzj.com/info/$id.html"

        return comic
    }

}
