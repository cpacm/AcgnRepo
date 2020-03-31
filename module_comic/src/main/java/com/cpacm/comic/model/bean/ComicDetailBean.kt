package com.cpacm.comic.model.bean

/**
 * 漫画内容列表
 * @author cpacm 2019-12-03
 */
class ComicDetailBean {


    /**
     * id : 41233
     * title : 圣女的魔力是万能的
     * is_dmzj : 0
     * cover : https://images.dmzj.com/webpic/2/180914sndmlswnd.jpg
     * description : 工作狂加班回家竟然被异世界召唤成为圣女！？
     * last_updatetime : 1574651978
     * last_update_chapter_name : 第16.5话
     * first_letter : s
     * comic_py : shengnvdemolishiwannengde
     * hot_num : 25776818
     * hit_num : 17737598
     * uid : null
     * last_update_chapter_id : 90582
     * authors : [{"tag_id":13617,"tag_name":"藤小豆"},{"tag_id":13618,"tag_name":"橘由华"}]
     * subscribe_num : 108108
     * chapters :
     */

    var id: String = ""
    var title: String? = null
    var cover: String? = null
    var description: String? = null
    var authors: String? = null //作者
    var category: String? = null //分类
    var status: String? = null //连载状态
    var hot: String? = null//热度
    var chapters: List<ComicVolumeBean>? = null //章节

    var webKey: String = ""

    var tags: ArrayList<String> = arrayListOf()//标签


}
