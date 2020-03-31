package com.cpacm.libarch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics

import java.lang.reflect.Field
import android.view.WindowManager
import androidx.core.content.FileProvider
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.cpacm.libarch.ArchApp
import com.cpacm.libarch.R
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


/**
 * 获取一些App系统的参数
 * Created by cpacm on 2015/6/27.
 */
object SystemParamsUtils {

    /**
     * 获取设备号
     *
     * @return
     */
    /*        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());*/
    val telephonyManager: TelephonyManager
        get() = ArchApp.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val handSetInfo: String
        get() = "手机型号:" + Build.MODEL +
                ",SDK版本:" + Build.VERSION.SDK_INT +
                ",系统版本:" + Build.VERSION.RELEASE +
                ",App版本：" + getAppVersion()

    /**
     * 检查网络是否连接
     *
     * @return
     */
    val isNetworkConnected: Boolean
        get() {
            val context = ArchApp.getInstance().getContext()
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                @Suppress("DEPRECATION")
                return mNetworkInfo.isAvailable
            }
            return false
        }

    fun getAppVersion(): String {
        val context = ArchApp.getInstance().getContext()
        return getAppVersionName(context)
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    fun getAppVersionName(context: Context): String {
        val manager = context.packageManager
        var appVersionName = ""
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            appVersionName = info.versionName // 版本名，versionCode同理
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appVersionName
    }

    @Suppress("DEPRECATION")
    fun getAppVersionCode(context: Context): Int {
        val manager = context.packageManager
        var appVersionCode = 1
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            appVersionCode = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appVersionCode
    }

    fun getAppChannel(context: Context, key: String): String? {
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val value = ai.metaData.get(key)
            if (value != null) {
                MoeLogger.v("渠道名：" + value.toString())
                return value.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    /**
     * 检查WIFI网络是否连接
     *
     * @param context
     * @return
     */
    @Suppress("DEPRECATION")
    fun isWIFIConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected
            }
        }
        return false
    }

    /**
     * 检查手机网络是否可用
     *
     * @param context
     * @return
     */
    @Suppress("DEPRECATION")
    fun isMobileConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * 设置文字到粘贴板
     *
     * @param context
     */
    fun setPrimaryClip(context: Context, text: String) {
        val c = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        c.setPrimaryClip(ClipData.newPlainText("bangumi", text))
    }

    fun getDisplayMetrics(context: Context): IntArray {
        if (context is Activity) {
            val dm = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(dm)
            return intArrayOf(dm.widthPixels, dm.heightPixels)
        } else {
            val dm2 = context.resources.displayMetrics
            return intArrayOf(dm2.widthPixels, dm2.heightPixels)
        }
    }

    /**
     * 获取系统状态栏高度
     * @param context
     * @return
     */
    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>?
        val obj: Any?
        val field: Field?
        val x: Int
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }

        return statusBarHeight
    }

    fun getScreenSize(context: Context): IntArray {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels         // 屏幕宽度（像素）
        val height = dm.heightPixels       // 屏幕高度（像素）
        //val density = dm.density         // 屏幕密度（0.75 / 1.0 / 1.5）
        //val densityDpi = dm.densityDpi     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        //val screenWidth = (width / density).toInt()  // 屏幕宽度(dp)
        //val screenHeight = (height / density).toInt()// 屏幕高度(dp)
        return intArrayOf(width, height)
    }

    /**
     * 分享字符串
     */
    fun shareString(context: Context, str: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, str)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.share)))
    }

    /**
     * 分享图片
     */
    fun shareDrawable(context: Context, drawable: Drawable?, fileName: String) {
        if (drawable == null) return
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream = FileOutputStream("$cachePath/$fileName", false) // overwrites this image every time
            if (drawable is GifDrawable) {
                val newGifDrawable = (drawable.constantState!!.newDrawable().mutate()) as GifDrawable
                val byteBuffer = newGifDrawable.buffer
                val bytes = ByteArray(byteBuffer.capacity())
                (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
                stream.write(bytes, 0, bytes.size)
            } else if (drawable is BitmapDrawable) {
                drawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
            stream.close()

            val imageFile = File(cachePath, fileName)
            val contentUri = FileProvider.getUriForFile(context, "com.cpacm.bangumi.fileprovider", imageFile)

            if (contentUri != null) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, "image/*")
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                context.startActivity(Intent.createChooser(shareIntent, "分享图片"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
