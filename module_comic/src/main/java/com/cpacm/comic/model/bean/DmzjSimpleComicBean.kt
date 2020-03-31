package com.cpacm.comic.model.bean

import com.cpacm.comic.ComicApp
import com.cpacm.comic.site.ComicSiteFactory

/**
 *
 *
 * dmzj 漫画实体类 简单版
 *
 * @author cpacm 2019-12-03
 */
class DmzjSimpleComicBean {

    /**
     * id : 50417
     * title : 在异世界上厕所
     * islong : 2
     * authors : Roots
     * types : 冒险/魔幻/欢乐向
     * cover : https://images.dmzj.com/webpic/4/191002ysjscs.jpg
     * status : 连载中
     * last_update_chapter_name : 第21话
     * last_update_chapter_id : 94792
     * last_updatetime : 1575356998
     */

    var id: Int = 0
        get() = if (field == 0) this.comic_id else field
    var title: String? = null
    var islong: Int = 0
    var authors: String? = null
    var types: String? = null
    var cover: String? = null
    var status: String? = null
    var last_update_chapter_name: String? = null
    var last_update_chapter_id: Long = 0
    var last_updatetime: Long = 0

    // 用于每日推荐的实体类
    var comic_id: Int = 0
        get() = if (field == 0) this.id else field

    var comic_py: String? = null
    var num: Int? = 0

    fun parse(): ComicBean {
        val comic = ComicBean()
        comic.id = id.toString()
        comic.title = title
        comic.status = status
        comic.category = types
        comic.cover = cover
        comic.author = authors?:"未知"
        var des = ""
        if (!last_update_chapter_name.isNullOrEmpty()) {
            des += "更新章节:${last_update_chapter_name}"
        }
        comic.description = des
        comic.website = "动漫之家"
        comic.webKey = ComicSiteFactory.COMIC_DMZJ
        comic.url = "https://m.dmzj.com/info/$id.html"

        return comic
    }

    //https://m.dmzj.com/info/50417.html
}
