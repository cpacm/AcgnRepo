package com.cpacm.comic.model.http

import com.cpacm.comic.ComicApp
import com.cpacm.libarch.model.http.BaseRetrofitHelper
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * <p>
 *     Mangabz漫画接口
 * @author cpacm 2019-12-10
 */
class MgbzRetrofitHelper private constructor() : BaseRetrofitHelper() {

    val mgbzApis: MgbzApis by lazy { getApiService(MgbzApis.BASE_URL, MgbzApis::class.java) }


    override fun addHeaders(request: Request): Request {
        return request
    }

    override fun customOkHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(ComicApp.getInstance().context))
        return builder.cookieJar(cookieJar)
    }

    companion object {

        private var ourInstance: MgbzRetrofitHelper? = null


        val instance: MgbzRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = MgbzRetrofitHelper()
                return ourInstance!!
            }
    }
}