package com.cpacm.libarch.utils

import android.util.Log

/**
 * Log帮助类
 * 请在发布时去掉注释
 * Created by Cpacm on 2015/5/15.
 */
object MoeLogger {

    private var TAG = "ACGN"

    //public static boolean DEBUG = BuildConfig.DEBUG;
    var DEBUG = true

    fun setTag(tag: String) {
        d("Changing log tag to %s", tag)
        TAG = tag
        DEBUG = Log.isLoggable(TAG, Log.VERBOSE)
    }

    fun d(format: String?, vararg args: Any?) {
        if (!DEBUG || format == null) return
        try {
            val msg = if (args.isEmpty()) format else String.format(format, *args)
            if (DEBUG)
                Log.d(TAG, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun v(format: String?, vararg args: Any?) {
        if (!DEBUG || format == null) return
        try {
            val msg = if (args.isEmpty()) format else String.format(format, *args)
            if (DEBUG)
                Log.v(TAG, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun e(format: String?, vararg args: Any?) {
        if (!DEBUG || format == null) return
        try {
            val msg = if (args.isEmpty()) format else String.format(format, *args)
            if (DEBUG)
                Log.e(TAG, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun e(msg: String?, e: Exception) {
        Log.e(TAG, msg, e)
    }

    fun wtf(format: String?, vararg args: Any?) {
        if (!DEBUG || format == null) return
        try {
            val msg = if (args.isEmpty()) format else String.format(format, *args)
            if (DEBUG)
                Log.wtf(TAG, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
