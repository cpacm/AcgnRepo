package com.cpacm.comic.model.http

import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.Request

/**
 * <p>
 *     漫画堆漫画接口
 * @author cpacm 2019-12-10
 */
class XXmhRetrofitHelper private constructor() : BaseRetrofitHelper() {

    val xxmhApis: XXmhApis by lazy { getApiService(XXmhApis.BASE_URL, XXmhApis::class.java) }

    override fun addHeaders(request: Request): Request {
        return request
    }

    companion object {

        private var ourInstance: XXmhRetrofitHelper? = null


        val instance: XXmhRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = XXmhRetrofitHelper()
                return ourInstance!!
            }
    }
}