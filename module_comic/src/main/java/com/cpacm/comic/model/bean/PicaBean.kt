package com.cpacm.comic.model.bean

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.http.PicaApis
import com.cpacm.comic.site.ComicSiteFactory

/**
 * <p>
 *
 * @author cpacm 2019-12-24
 */

data class PicaTokenBean(var token: String?)

/**
 * {
"status": "ok",
"addresses": ["104.20.180.50", "104.20.181.50"],
"waka": "https://ad-channel.woyeahgo.tk",
"adKeyword": "woyeahgo"
}
 */
class PicaAddressBean {
    var status: String? = null
    var addresses: List<String>? = null
}

/**
 *
 * {
"isPunched": false,
"latestApplication": {
"_id": "5dc58b151e103c60e7663b12",
"downloadUrl": "https://download.woyeahgo.tk/apps/2.2.1.3.3.4_collections.apk",
"updateContent": "【一般更新】\n\n1・新增漫畫推薦欄\n\n2・修改部份版本閃退問題\n\n後備下載連結\nhttps://download.woyeahgo.tk/apps/2.2.1.3.3.4_collections.apk",
"version": "2.2.1.3.3.4",
"updated_at": "2019-11-08T15:38:45.706Z",
"created_at": "2019-11-08T15:34:45.163Z",
"apk": {
"originalName": "2.2.1.3.3.4_collections.apk",
"path": "4da05b12-3534-4b4d-b9bf-804de301d2e0.apk",
"fileServer": "https://storage1.picacomic.com"
}
},
"imageServer": "https://s3.picacomic.com/static/",
"categories": [
{
"_id": "5821859b5f6b9a4f93dbf6e9",
"title": "嗶咔漢化"
},
{
"_id": "5821859b5f6b9a4f93dbf6d1",
"title": "全彩"
}
}
 */
data class PicaInitialBean(var imageServer: String, val latestApplication: PicaLastAppBean, val categories: ArrayList<PicaCategoryBean>)

data class PicaLastAppBean(val downloadUrl: String, val updateContent: String, val version: String)

/**
https://s3.picacomic.com/static/
https://media.wakamoment.tk/static/
{
"_id": "5821859b5f6b9a4f93dbf6e6",
"title": "Cosplay",
"description": "未知",
"thumb": {
"originalName": "Cosplay.jpg",
"path": "24ee03b1-ad3d-4c6b-9f0f-83cc95365006.jpg",
"fileServer": "https://storage1.picacomic.com"
}
{
"title": "嗶咔運動",
"thumb": {
"originalName": "picacomic-move-cat.jpg",
"path": "picacomic-move-cat.jpg",
"fileServer": "https://pica-web.wakamoment.tk/static/"
},
"isWeb": true,
"active": true,
"link": "https://move-web.wakamoment.tk"
}
援助嗶咔 - https://donate.wakamoment.tk
嗶咔運動 - https://move-web.wakamoment.tk
小里番 - https://h.woyeahgo.tk
小電影 - https://av.woyeahgo.tk
嗶咔鍋貼 - https://post-web.woyeahgo.tk
還我收藏 - https://move-fav.wakamoment.tk
 */
data class PicaCategoryBean(val _id: String?, val title: String?, val description: String?, val thumb: PicaThumbBean?,
                            val isWeb: Boolean = false, val active: Boolean = true, val link: String?)

data class PicaCategoryListBean(val categories: List<PicaCategoryBean>)

data class PicaThumbBean(val originalName: String?, val path: String?, val fileServer: String?)


data class PicaComicListBean(val comics: PicaDocBean<PicaComicBean>)

data class PicaDocBean<T>(val docs: List<T>, val total: Int, val limit: Int, val page: Int, val pages: Int)

data class PicaComicRankBean(val comics: List<PicaComicBean>)

class PicaComicBean(val _id: String,
                    val title: String,
                    val author: String,
                    val pagesCount: Int,
                    val epsCount: Int,
                    val finished: Boolean,
                    val categories: List<String>,
                    val thumb: PicaThumbBean?,
                    val totalLikes: Int,
                    val totalViews: Int,
                    val id: String?,
                    val likesCount: Int) {
    fun parse(imageServer: String?): ComicBean {
        val comic = ComicBean()
        comic.id = _id
        comic.title = title
        comic.status = if (finished) "完结" else "连载中"
        comic.category = categories.toString()
        comic.cover = getCoverThumb(thumb, imageServer)
        comic.author = author
        val des = "总章节:[${epsCount}]，喜欢：${totalLikes}"
        comic.description = des
        comic.website = "哔咔漫画"
        comic.webKey = ComicSiteFactory.COMIC_PICA
        comic.url = PicaApis.BASE_URL + "comics/${_id}"

        return comic
    }

    fun getCoverThumb(thumb: PicaThumbBean?, imageServer: String?): String {
        if (thumb == null) {
            return ""
        }
        return if (thumb.fileServer.equals("http://lorempixel.com")) {
            thumb.fileServer + thumb.path
        } else if (imageServer.isNullOrEmpty()) {
            thumb.fileServer + "/static/" + thumb.path
        } else {
            imageServer + thumb.path
        }
    }
}

/**
{
"_id": "5da89805fc84c83fc017586b",
"_creator": "",
"title": "よごとひめごと",
"description": "各种萝莉本\n（有地雷的已标记）\n(后面大概有人发过，就不发了)\n(羡慕嫉妒恨——)",
"thumb": {
"originalName": "2.jpg",
"path": "tobeimg/pdbMnI-YHgVtmwQbrDhFY5zFYvpHDCDrPTKFelY5_GU/fill/300/400/sm/0/aHR0cHM6Ly9zdG9yYWdlMS5waWNhY29taWMuY29tL3N0YXRpYy9kOTQyMmViYS1jMGM1LTQ1OGUtOWQyYS1lMGViZGI5NGY2ODAuanBn.jpg",
"fileServer": "https://storage1.picacomic.com"
},
"author": "雪野みなと",
"chineseTeam": "嗶咔嗶咔漢化組",
"categories": [
"長篇",
"嗶咔漢化",
"純愛",
"妹妹系",
"後宮閃光",
"強暴"],
"tags": [
"小學女生(JS)   ",
"近親相交: 父",
"近親相交：父",
"近親相交: 妹",
"妹妹",
"蘿莉",
"正太",
"中出",
"肛交",
"師生戀",
"老師"],
"pagesCount": 105,
"epsCount": 6,
"finished": false,
"updated_at": "2019-10-17T16:34:13.586Z",
"created_at": "2018-12-07T01:07:28.949Z",
"allowDownload": true,
"allowComment": true,
"totalLikes": 1341,
"totalViews": 51835,
"viewsCount": 51835,
"likesCount": 1343,
"isFavourite": false,
"isLiked": false,
"commentsCount": 50
}
 */
class PicaComicDetailBean(
        val _id: String,
        //val _creator:Creator?, 创建者
        val title: String,
        val description: String?,
        val thumb: PicaThumbBean?,
        val author: String?,
        val chineseTeam: String,
        val categories: List<String>?,
        val tags: List<String>?,
        val pagesCount: Int,
        val epsCount: Int,
        val finished: Boolean,
        val updated_at: String,
        val totalLikes: Int,
        val totalViews: Int,
        val viewsCount: Int,
        val likesCount: Int) {
    fun parse(imageServer: String?): ComicDetailBean {
        val comic = ComicDetailBean()
        var info = "作者："
        if (!author.isNullOrEmpty()) {
            info += author
        } else {
            info += "未知"
        }
        comic.authors = info

        info = "分类："
        if (!categories.isNullOrEmpty()) {
            for (type in categories) {
                info += type.trim() + "  "
            }
        } else {
            info += "未分类"
        }
        comic.category = info

        if (!tags.isNullOrEmpty()) {
            comic.tags.addAll(tags)
        }
        comic.hot = "喜欢数：$totalLikes"
        comic.cover = getCoverThumb(thumb, imageServer)
        comic.description = if (description.isNullOrEmpty()) "暂无描述" else description
        comic.status = if (finished) "完结" else "连载中"

        //comic.chapters = volumeList
        comic.title = title
        comic.id = _id
        comic.webKey = ComicSiteFactory.COMIC_PICA

        return comic
    }

    fun getCoverThumb(thumb: PicaThumbBean?, imageServer: String?): String {
        if (thumb == null) {
            return ""
        }
        return if (thumb.fileServer.equals("http://lorempixel.com")) {
            thumb.fileServer + thumb.path
        } else if (imageServer.isNullOrEmpty()) {
            thumb.fileServer + "/static/" + thumb.path
        } else {
            imageServer + thumb.path
        }
    }
}

data class PicaComicDetail(val comic: PicaComicDetailBean)

data class PicaEpListBean(val eps: PicaDocBean<PicaEpBean>)

/**
{
"_id": "5dceb2e217be2902eca7306b",
"title": "第1話",
"order": 1,
"updated_at": "2019-11-15T09:12:05.416Z",
"id": "5dceb2e217be2902eca7306b"
}
 */
data class PicaEpBean(
        val _id: String,
        val title: String,
        val order: Int,
        val date: String,
        val id: String?
) {
    fun parse(): ComicChapterBean {
        val chapter = ComicChapterBean()
        chapter.chapterId = order.toString()
        chapter.chapterTitle = title
        chapter.isVolume = false
        return chapter
    }
}

data class PicaContentListBean(val pages: PicaDocBean<PicaContentBean>)

/**
{
"_id": "5dceb2e217be2902eca7308b",
"media": {
"originalName": "031.jpg",
"path": "tobeimg/jks_GBTcDuSC_CeJp4r2RFptMr4YRcWaG7xQUK2aEMc/fit/800/800/ce/0/aHR0cHM6Ly9zdG9yYWdlMS5waWNhY29taWMuY29tL3N0YXRpYy9hZmVmYWIwMy1jYTJiLTQ2NWEtOGFhZC0yMmY3MmQ5ODlmZTIuanBn.jpg",
"fileServer": "https://storage1.picacomic.com"
},
"id": "5dceb2e217be2902eca7308b"
}
 **/
data class PicaContentBean(val _id: String, val media: PicaThumbBean?, val id: String?) {
    fun getCoverThumb(imageServer: String?): String {
        if (media == null) {
            return ""
        }
        return if (media.fileServer.equals("http://lorempixel.com")) {
            media.fileServer + media.path
        } else if (imageServer.isNullOrEmpty()) {
            media.fileServer + "/static/" + media.path
        } else {
            imageServer + media.path
        }
    }
}

/**
{
"_id": "583ed6002b70b14d72f660f6",
"gender": "m",
"name": "二次元辐射",
"slogan": "null",
"title": "可是我覺得你好美",
"verified": false,
"exp": 24684,
"level": 16,
"characters": [
"knight"],
"role": "knight",
"avatar": {
"originalName": "avatar.jpg",
"path": "eb7ad5c9-921a-4c81-894b-aa267eca9ab2.jpg",
"fileServer": "https://storage1.picacomic.com",
},
"character": "https://pica-web.wakamoment.tk/special/frame-456.png"
}
 **/
data class PicaCreator(val name: String)

data class SortBean(val keyword: String, val sort: String)