package com.cpacm.acgnrepo

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.cpacm.libarch.utils.FileUtils


/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: glide配置
 */
@GlideModule
class AcgGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //设置内存缓存大小
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()//获取系统分配给应用的总内存大小
        val memoryCacheSize = maxMemory / 8L//设置图片内存缓存占用八分之一
        builder.setMemoryCache(LruResourceCache(memoryCacheSize))

        val diskCacheSize = 1024 * 1024 * 1024L//最多可以缓存多少字节的数据,1024M
        val path = FileUtils.cacheDir
        builder.setDiskCache(DiskLruCacheFactory(path, "glide", diskCacheSize))
        //builder.setDiskCache(new InternalCacheDiskCacheFactory(context,"cacheDirectoryName", 200*1024*1024));//设置文件缓存的大小为200M
        //builder.setMemoryCache(new LruResourceCache(yourSizeInBytes));//设置内存缓存大小
        //builder.setBitmapPool(new LruBitmapPool(sizeInBytes));//bitmap池大小
        //builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);//bitmap格式 default is RGB_565
    }


}
