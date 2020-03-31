package com.cpacm.libarch.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment

import java.io.File
import java.util.regex.Pattern

import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.core.content.FileProvider
import android.os.Build
import android.provider.MediaStore
import com.cpacm.libarch.ArchApp
import java.io.FileOutputStream
import java.io.IOException


/**
 * manage file include download , upload , read ,write
 */
object FileUtils {

    private val DELAY_TIME = 10000

    val CACHE_DIR = "bangumi"
    val GLIDE_CACHE_DIR = "glide"
    val SONG_CACHE_DIR = "songs"
    val NETWORK_CACHE_DIR = "netcache"

    val APK_NAME = "bangumi.apk"

    /**
     * http 请求缓存空间
     *
     * @return
     */
    val netCacheDir: String
        get() = cacheDir + File.separator + NETWORK_CACHE_DIR

    /**
     * 获取缓存主目录
     *
     * @return
     */
    // 创建一个文件夹对象，赋值为外部存储器的目录
    //得到一个路径，内容是sdcard的文件夹路径和名字
    val mountedCacheDir: String?
        get() {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val sdcardDir = Environment.getExternalStorageDirectory()
                val path = sdcardDir.path + File.separator + CACHE_DIR
                val path1 = File(path)
                if (!path1.exists()) {
                    path1.mkdirs()
                }
                return path1.path
            }
            return null
        }

    /**
     * 获取存放歌曲的目录
     *
     * @return
     */
    val songDir: String?
        get() {
            val path = downloadDir + File.separator + SONG_CACHE_DIR
            val path1 = File(path)
            if (!path1.exists()) {
                path1.mkdirs()
            }
            return path1.path
        }

    /**
     * 获取存放缓存的目录
     *
     * @return
     */
    val cacheDir: String
        get() {
            val file = cacheFile
            return file.absolutePath
        }

    val cacheFile: File
        get() {
            var fileDir: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                fileDir = ArchApp.getInstance().getContext().externalCacheDir
            }
            if (fileDir == null) {
                fileDir = ArchApp.getInstance().getContext().cacheDir
            }

            return fileDir!!
        }

    /**
     * 获取下载数据的缓存地址
     *
     * @return
     */
    val downloadDir: String
        get() = downloadFile.absolutePath

    private val downloadFile: File
        get() {
            var fileDir: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                fileDir = ArchApp.getInstance().getContext().getExternalFilesDir(DIRECTORY_DOWNLOADS)
            }

            if (fileDir == null) {
                fileDir = ArchApp.getInstance().getContext().getDir(DIRECTORY_DOWNLOADS, Context.MODE_PRIVATE)
            }
            return fileDir!!
        }


    /**
     * 获取apk放置的地址
     *
     * @return
     */
    val apkPath: String
        get() {
            val apkPath = downloadDir + File.separator + APK_NAME
            val file = File(apkPath)
            if (file.exists()) {
                file.delete()
            }
            return file.path
        }

    private val FilePattern = Pattern.compile("[\\\\/:*?\"<>|]")

    /**
     * 应用关联的图片存储空间
     *
     * @param context
     * @return
     */
    fun getAppPictureDir(context: Context): String {
        val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return file!!.path
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件<br></br>
     * 支持两级目录删除
     */
    fun cleanCacheDir() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val directory = ArchApp.getInstance().getContext().externalCacheDir
            if (directory != null && directory.exists() && directory.isDirectory) {
                for (item in directory.listFiles()) {
                    if (item.isDirectory) {
                        for (img in item.listFiles()) {
                            img.delete()
                        }
                    }
                    item.delete()
                }
            }
        }
    }

    /**
     * 读取Assets目录下的文件
     *
     * @param context
     * @param name
     * @return
     */
    fun getAssets(context: Context, name: String): String? {
        var result: String? = null
        try {
            val `in` = context.assets.open(name)  //获得AssetManger 对象, 调用其open 方法取得  对应的inputStream对象
            val size = `in`.available()//取得数据流的数据大小
            val buffer = ByteArray(size)
            `in`.read(buffer)
            `in`.close()
            result = String(buffer)
        } catch (e: Exception) {
            MoeLogger.d("getAssets:" + result!!)
        }

        return result
    }

    /**
     * 媒体扫描，防止下载后在sdcard中获取不到歌曲的信息
     *
     * @param path
     */
    fun mp3Scanner(path: String) {
        MediaScannerConnection.scanFile(ArchApp.getInstance().getContext().applicationContext,
                arrayOf(path), null, null)
    }

    fun existFile(path: String): Boolean {
        val path1 = File(path)
        return path1.exists()
    }

    fun deleteFile(path: String): Boolean {
        val path1 = File(path)
        return if (path1.exists()) {
            path1.delete()
        } else false
    }

    /**
     * open apk
     *
     * @param context
     * @param apk
     */
    fun openApk(context: Context, appId:String, apk: File) {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(
                    context, "${appId}.fileprovider", apk)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive")
        }

        context.startActivity(intent)
    }

    @SuppressLint("CheckResult")
    fun saveWebImage(c: Context, url: String, bitmap: Bitmap) {
        try {
            val str = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val fileName = str[str.size - 1]
            val filePath = getSystemImagePath()
            val dstPath = filePath + fileName

            bitmap2File(bitmap, filePath, fileName)

            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATA, dstPath)
            c.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } catch (e: Exception) {
        }

    }

    @Throws(IOException::class)
    fun bitmap2File(bitmap: Bitmap, filePath: String, fileName: String): File {
        val dir = File(filePath)
        dir.mkdirs()

        val file = File(filePath + fileName)
        file.createNewFile()

        val fos = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file
    }


    fun getSystemImagePath(): String {
        val picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        return "$picturePath/bangumi/"
    }
}
