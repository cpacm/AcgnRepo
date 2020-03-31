package com.cpacm.acgnrepo

import android.app.Application
import com.cpacm.comic.ComicApp
import com.cpacm.libarch.ArchApp

/**
 * <p>
 *
 * @author cpacm 2020/3/31
 */
class AcgnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ArchApp.register(this)
        ComicApp.register(this)
    }

}