package com.cpacm.comic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.cpacm.comic.site.ComicSiteFactory

/**
 * <p>
 *     漫画阅读模块
 * @author cpacm 2019/8/23
 */
class ComicApp private constructor(application: Application) {

    var context: Context = application
    var comicSite: ComicSiteFactory? = null

    init {
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: ComicApp? = null

        fun getInstance(): ComicApp {
            if (instance == null) throw NullPointerException("please register Comic module before use!!")
            return instance!!
        }

        /**
         * 必须在Application onCreate中调用，以便初始化本模块。
         */
        fun register(application: Application) {
            instance = ComicApp(application)
        }

    }
}