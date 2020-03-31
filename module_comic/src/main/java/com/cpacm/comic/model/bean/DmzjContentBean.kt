package com.cpacm.comic.model.bean

/**
 *
 * 动漫之家每一章的漫画内容
 * @author cpacm 2019-12-03
 */
class DmzjContentBean {

    /**
     * chapter_id : 84701
     * comic_id : 41233
     * title : 第01卷
     * chapter_order : 1
     * direction : 1
     * page_url : ["http://imgsmall.dmzj.com/s/41233/84701/0.jpg","http://imgsmall.dmzj.com/s/41233/84701/1.jpg","http://imgsmall.dmzj.com/s/41233/84701/2.jpg","http://imgsmall.dmzj.com/s/41233/84701/3.jpg","http://imgsmall.dmzj.com/s/41233/84701/170.jpg","http://imgsmall.dmzj.com/s/41233/84701/171.jpg","http://imgsmall.dmzj.com/s/41233/84701/172.jpg"]
     * picnum : 173
     * comment_count : 23
     */

    var chapter_id: Long = 0
    var comic_id: Int = 0
    var title: String? = null
    var chapter_order: Int = 0
    var direction: Int = 0
    var picnum: Int = 0
    var comment_count: Int = 0
    var page_url: List<String>? = null

    fun parse(): ComicContentBean {
        val content = ComicContentBean()
        content.chapterId = chapter_id.toString()
        content.comicId = comic_id.toString()
        content.pageUrl = page_url
        content.picNum = picnum
        content.title = title
        return content
    }

}
