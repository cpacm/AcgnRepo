package com.cpacm.comic.model.http

import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.Request

/**
 * <p>
 *     漫画接口
 * @author cpacm 2019-12-10
 */
class DmzjRetrofitHelper private constructor() : BaseRetrofitHelper() {

    val dmzjApi: DmzjApis by lazy { getApiService(DmzjApis.BASE_URL, DmzjApis::class.java) }

    override fun addHeaders(request: Request): Request {
        return request
    }

    companion object {

        private var ourInstance: DmzjRetrofitHelper? = null


        val instance: DmzjRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = DmzjRetrofitHelper()
                return ourInstance!!
            }
    }
}