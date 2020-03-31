package com.cpacm.comic.model.http

import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress

/**
 * <p>
 *     漫画堆漫画接口
 * @author cpacm 2019-12-10
 */
class MhDuiRetrofitHelper private constructor() : BaseRetrofitHelper() {

    val mhDuiApi: MhDuiApis by lazy { getApiService(MhDuiApis.BASE_URL, MhDuiApis::class.java) }

    override fun addHeaders(request: Request): Request {
        return request
    }

    override fun customOkHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.dns(ManhuaduiDns())
        return super.customOkHttpClientBuilder(builder)
    }

    companion object {

        private var ourInstance: MhDuiRetrofitHelper? = null


        val instance: MhDuiRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = MhDuiRetrofitHelper()
                return ourInstance!!
            }
    }

    class ManhuaduiDns : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            try {
                val arrayList = arrayListOf<InetAddress>()
                arrayList.addAll(InetAddress.getAllByName("118.107.43.36"))
                arrayList.addAll(Dns.SYSTEM.lookup(hostname))
                return arrayList
            } catch (e: Exception) {
                e.printStackTrace()
                return Dns.SYSTEM.lookup(hostname)
            }
        }
    }
}