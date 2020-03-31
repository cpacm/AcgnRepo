package com.cpacm.comic.model.bean

import com.cpacm.comic.ComicApp
import com.cpacm.comic.site.ComicSiteFactory

/**
 * 动漫之家漫画内容详情
 * @author cpacm 2019-12-03
 */
class DmzjDetailComicBean {


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

    var id: Int = 0
    var title: String? = null
    var cover: String? = null
    var description: String? = null
    var last_updatetime: Long = 0
    var last_update_chapter_name: String? = null
    var first_letter: String? = null
    var comic_py: String? = null
    var hot_num: Int = 0
    var hit_num: Int = 0
    var last_update_chapter_id: Long = 0
    var subscribe_num: Int = 0
    var chapters: List<DmzjVolumeBean>? = null
    var authors: List<DmzjTagBean>? = null
    var types: List<DmzjTagBean>? = null
    var status: List<DmzjTagBean>? = null


    fun parse(): ComicDetailBean {
        val comic = ComicDetailBean()
        var info = "作者："
        if (!authors.isNullOrEmpty()) {
            for (author in authors!!) {
                info += author.tag_name + "  "
            }
        } else {
            info += "未知"
        }
        comic.authors = info

        info = "分类："
        if (!types.isNullOrEmpty()) {
            for (type in types!!) {
                info += type.tag_name + "  "
            }
        } else {
            info += "未分类"
        }
        comic.category = info
        comic.hot = "热度：$hot_num"
        comic.cover = cover
        comic.description = if (description.isNullOrEmpty()) "暂无描述" else description

        info = ""
        if (!status.isNullOrEmpty()) {
            for (type in status!!) {
                info += type.tag_name + "  "
            }
        }
        comic.status = info


        val volumeList = arrayListOf<ComicVolumeBean>()
        if (!chapters.isNullOrEmpty()) {
            for (volume in chapters!!) {
                volumeList.add(volume.parse())
            }
        }
        comic.chapters = volumeList
        comic.title = title
        comic.id = id.toString()
        comic.webKey = ComicSiteFactory.COMIC_DMZJ
        return comic
    }

}
