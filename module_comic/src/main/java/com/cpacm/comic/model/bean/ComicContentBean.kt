package com.cpacm.comic.model.bean

/**
 *
 * 每一章的漫画内容
 * @author cpacm 2019-12-20
 */
class ComicContentBean {

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

    var chapterId: String = "0"
    var comicId: String = "0"
    var title: String? = null
    var picNum: Int = 0
    var pageUrl: List<String>? = null

}
