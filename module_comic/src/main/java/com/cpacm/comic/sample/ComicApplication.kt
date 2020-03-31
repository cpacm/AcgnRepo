package com.cpacm.comic.sample

import android.app.Application
import com.cpacm.comic.ComicApp
import com.cpacm.libarch.ArchApp

/**
 * <p>
 *
 * @author cpacm 2019-11-21
 */
class ComicApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        ArchApp.register(this)

        ComicApp.register(this)

    }
}