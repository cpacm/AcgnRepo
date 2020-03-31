package com.cpacm.comic.utils

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.ModelLoader
import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.site.dmzj.DmzjSite
import com.cpacm.comic.site.xxmh.XXmhSite
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.io.InputStream
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 * <p>
 *
 * @author cpacm 2019-12-26
 */
class ComicModelLoader : ModelLoader<String, InputStream> {
    var client = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            //.connectTimeout(20, TimeUnit.SECONDS)
            .dns(PicaThumbDns())
            .build()


    private val okhttpLoader: OkHttpUrlLoader

    init {
        okhttpLoader = OkHttpUrlLoader(client)
    }

    override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        val glideUrl = if (model.contains("dmzj.com") || ComicApp.getInstance().comicSite!!.javaClass==DmzjSite::class.java) {
            GlideUrl(model, LazyHeaders.Builder().addHeader("Referer", "https://www.dmzj.com").build())
        } else if (model.contains("hws.gdbyhtl.net") || ComicApp.getInstance().comicSite!!.javaClass==XXmhSite::class.java) {
            GlideUrl(model, LazyHeaders.Builder().addHeader("Referer", "https://www.177mh.net/").build())
        } else {
            GlideUrl(model, LazyHeaders.Builder().build())
        }
        return okhttpLoader.buildLoadData(glideUrl, width, height, options)
    }

    override fun handles(model: String): Boolean {
        return true
    }
}