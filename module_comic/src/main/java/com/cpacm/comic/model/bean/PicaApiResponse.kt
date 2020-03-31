package com.cpacm.comic.model.bean

/**
 * @author: cpacm
 * @date: 2017/2/16
 * @desciption:
 */

class PicaApiResponse<T> {
    var code: Int = 0
    var data: T? = null
    var message: String? = null
}
