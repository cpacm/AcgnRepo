package com.cpacm.comic.model.http

import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.Request

/**
 * <p>
 *     漫画堆漫画接口
 * @author cpacm 2019-12-10
 */
class Mh123RetrofitHelper private constructor() : BaseRetrofitHelper() {

    val mh123Apis: Mh123Apis by lazy { getApiService(Mh123Apis.BASE_URL, Mh123Apis::class.java) }

    override fun addHeaders(request: Request): Request {
        return request
    }

    companion object {

        private var ourInstance: Mh123RetrofitHelper? = null


        val instance: Mh123RetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = Mh123RetrofitHelper()
                return ourInstance!!
            }
    }
}