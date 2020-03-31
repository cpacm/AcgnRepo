package com.cpacm.comic.model.bean

/**
 * <p>
 *
 * @author cpacm 2019-12-18
 */
class ComicBean {

    var id: String = "0"//comic id
    var title: String? = null//标题
    var author: String? = null//作者
    var category: String? = null//分类
    var status: String? = null//状态，位于标题右侧
    var description: String? = null //描述，位于下侧
    var cover: String? = null//封面

    var website: String? = null//网站名
    var webKey: String? = null//网站缩写，如dmzj,hh
    var url:String?=null//网站链接
}