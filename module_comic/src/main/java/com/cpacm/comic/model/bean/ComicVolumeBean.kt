package com.cpacm.comic.model.bean

/**
 *
 *
 * 卷内容
 *
 * @author cpacm 2019-12-03
 */
class ComicVolumeBean {

    /**
     * title : 单行本
     * data : [{"chapter_id":84702,"chapter_title":"2卷","updatetime":1559878482,"filesize":22570027,"chapter_order":2},{"chapter_id":84701,"chapter_title":"1卷","updatetime":1559878462,"filesize":23757404,"chapter_order":1}]
     */

    var title: String? = null
    var content: ArrayList<ComicChapterBean>? = null
}
