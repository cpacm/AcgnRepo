package com.cpacm.comic.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.LibraryGlideModule
import java.io.InputStream


/**
 * <p>
 *
 * @author cpacm 2019-12-26
 */
@GlideModule
class ComicGlideModule : LibraryGlideModule() {
//class ComicGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(String::class.java, InputStream::class.java, Factory())
    }

    class Factory : ModelLoaderFactory<String, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return ComicModelLoader()
        }

        override fun teardown() {}
    }
}