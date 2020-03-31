package com.cpacm.libarch.model.http

import com.cpacm.libarch.utils.FileUtils
import com.cpacm.libarch.utils.MoeLogger
import com.cpacm.libarch.utils.SystemParamsUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

import java.io.File

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @auther: cpacm
 * @date: 2017/-2/04
 * @desciption: Retrofit初始化
 */
abstract class BaseRetrofitHelper {

    private var okHttpClient: OkHttpClient? = null

    init {
        initOkHttp()
    }

    private fun initOkHttp() {
        val builder = OkHttpClient.Builder()

        val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                MoeLogger.d(message)
            }
        })
        logInterceptor.level = if (MoeLogger.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val cacheFile = File(FileUtils.netCacheDir)
        val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
        val requestHeadersInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val newBuilder = request.newBuilder()
                // builder.header("Content-Type", "application/json;charset=UTF-8")
                //       .header("Accept-Charset", "UTF-8");
                if (!SystemParamsUtils.isNetworkConnected) {
                    newBuilder.cacheControl(CacheControl.FORCE_CACHE)
                }
                return chain.proceed(newBuilder.build())
            }
        }


        val cacheInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())
                if (SystemParamsUtils.isNetworkConnected) {
                    val maxAge = 60 * 30
                    // 有网络时, 不缓存, 最大保存时长为30分
                    response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build()
                }
                return response
            }
        }
        // 一些静态的header
        val apikey = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = addHeaders(chain.request())
                return chain.proceed(request)
            }
        }
        //设置统一的请求头部参数
        builder.addInterceptor(apikey)
                .cache(cache)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(cacheInterceptor) //设置缓存
                .addInterceptor(requestHeadersInterceptor)
                .retryOnConnectionFailure(true)//错误重连
        customOkHttpClientBuilder(builder)
        //设置超时
        //builder.connectTimeout(10, TimeUnit.SECONDS)
        //builder.readTimeout(20, TimeUnit.SECONDS)
        //builder.writeTimeout(20, TimeUnit.SECONDS)
        okHttpClient = builder.build()
    }

    protected open fun customOkHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
    }

    protected open fun addHeaders(request: Request): Request {
        val builder = request.newBuilder()
        return builder.build()
    }

    fun <T> getApiService(baseUrl: String, clz: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient!!)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(clz)
    }

    fun <T> getSimpleGSonApiService(baseUrl: String, clz: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(clz)
    }

}
