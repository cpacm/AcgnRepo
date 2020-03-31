package com.cpacm.comic.model.http

import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.ComicSettingManager.Companion.KEYWORD_COMIC_PICA_ADDRESS
import com.cpacm.comic.model.ComicSettingManager.Companion.KEYWORD_COMIC_PICA_SERVER
import com.cpacm.libarch.model.http.BaseRetrofitHelper
import okhttp3.CertificatePinner
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.security.InvalidKeyException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * <p>
 *     漫画接口
 * @author cpacm 2019-12-10
 */
class PicaRetrofitHelper private constructor() : BaseRetrofitHelper() {

    var picaApi: PicaApis = getApiService(PicaApis.BASE_URL, PicaApis::class.java)
    var picaAddressApi: PicaApis.PicaAddressApis = getSimpleGSonApiService(PicaApis.BASE_URL, PicaApis.PicaAddressApis::class.java)

    override fun addHeaders(request: Request): Request {
        //参数加密
        /**
        :method: POST
        :path: /auth/sign-in
        :authority: picaapi.picacomic.com
        :scheme: https
        api-key: C69BAF41DA5ABD1FFEDC6D2FEA56B
        accept: application/vnd.picacomic.com.v1+json
        app-channel: 2
        time: 1577295413
        nonce: 55c28cd307d147eda78a3f03000c94d5
        signature: a138cbae3509bb13ac41c403142a9586092bfec415d099cf1379c23f82682215
        app-version: 2.2.1.3.3.4
        app-uuid: defaultUuid
        image-quality: medium
        app-platform: android
        app-build-version: 45
        content-type: application/json; charset=UTF-8
        content-length: 43
        accept-encoding: gzip
        user-agent: okhttp/3.8.1


         */
        val method = request.method
        val time = (System.currentTimeMillis() / 1000).toString()
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val api_key = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
        val secret_key = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"
        var url = request.url.toString()
        url = url.replace(PicaApis.BASE_URL, "")
        url = url + time + nonce + method + api_key
        url = url.toLowerCase(Locale.CHINA)
        val signature = HMACSHA256(url.toByteArray(), secret_key.toByteArray())

        val builder = request.newBuilder()

        //添加用户注册信息
        val address = ComicSettingManager.getInstance().getSetting(KEYWORD_COMIC_PICA_ADDRESS, "")
        val key = ComicSettingManager.KEYWORD_COMIC_AUTHORITY + "-" + address
        val auth = ComicSettingManager.getInstance().getSetting(key)
        if (auth.isNotEmpty()) {
            builder.addHeader("authorization", auth)
        }
        val channel = ComicSettingManager.getInstance().getSetting(KEYWORD_COMIC_PICA_SERVER, 1)

        val newRequest = builder
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Host", PicaApis.PICA_BASE_HOST)
                .addHeader("User-Agent", "okhttp/3.8.1")
                .addHeader("accept", "application/vnd.picacomic.com.v1+json")
                .addHeader("api-key", api_key)
                .addHeader("app-build-version", "45")
                .addHeader("app-version", "2.2.1.3.3.4")
                .addHeader("app-channel", channel.toString())
                .addHeader("app-platform", "android")
                .addHeader("app-uuid", "defaultUuid")
                .addHeader("nonce", nonce)
                .addHeader("sources", "beta anime")
                .addHeader("time", time)
                .addHeader("signature", signature)
                .addHeader("image-quality", "0") //0-origin 1-low 2-medium 3-high
                .build()
        return newRequest
    }

    override fun customOkHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.dns(PicaDns())
        CertificatePinner.Builder().build()
        try {
            val fVar = PicaTLSSocketFactory()
            builder.sslSocketFactory(fVar, fVar.systemDefaultTrustManager()!!)
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return super.customOkHttpClientBuilder(builder)
    }


    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun HMACSHA256(data: ByteArray, key: ByteArray): String {
        val signingKey = SecretKeySpec(key, "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)
        return bytesToHexString(mac.doFinal(data))
    }

    fun bytesToHexString(src: ByteArray): String {
        val stringBuilder = StringBuilder("")
        for (i in 0 until src.size) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    class PicaDns : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            val address = ComicSettingManager.getInstance().getSetting(KEYWORD_COMIC_PICA_ADDRESS, "")
            try {
                val arrayList = arrayListOf<InetAddress>()
                if (address.isEmpty()) {
                    return Dns.SYSTEM.lookup(hostname)
                }
                //address.add(hostname)
                arrayList.addAll(InetAddress.getAllByName(address))
                //arrayList.addAll(Dns.SYSTEM.lookup(hostname))
                return arrayList
            } catch (e: Exception) {
                e.printStackTrace()
                return Dns.SYSTEM.lookup(hostname)
            }
        }
    }


    companion object {
        private var ourInstance: PicaRetrofitHelper? = null

        val instance: PicaRetrofitHelper
            get() {
                if (ourInstance == null)
                    ourInstance = PicaRetrofitHelper()
                return ourInstance!!
            }
    }
}