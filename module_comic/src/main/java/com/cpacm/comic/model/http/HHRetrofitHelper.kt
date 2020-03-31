package com.cpacm.comic.model.http

import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.Request

/**
 * <p>
 *     汗汗漫画接口
 * @author cpacm 2019-12-10
 */
class HHRetrofitHelper private constructor() : BaseRetrofitHelper() {

    val hhApi: HHApis by lazy { getApiService(HHApis.BASE_URL, HHApis::class.java) }

    override fun addHeaders(request: Request): Request {
        return request
    }

    companion object {

        private var ourInstance: HHRetrofitHelper? = null


        val instance: HHRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = HHRetrofitHelper()
                return ourInstance!!
            }
    }
}