package com.cpacm.libarch.model

/**
 * @author: cpacm
 * @date: 2017/2/16
 * @desciption:
 */

class ApiResponse<T> {
    var code: Int = 0
    var data: T? = null
    var message: String? = null
}
