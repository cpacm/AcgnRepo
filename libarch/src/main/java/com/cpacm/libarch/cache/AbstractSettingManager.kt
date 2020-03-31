package com.cpacm.libarch.cache

import android.content.Context
import android.content.SharedPreferences
import com.cpacm.libarch.ArchApp

/**
 * <p>
 *
 * @author cpacm 2019-11-22
 */
abstract class AbstractSettingManager {

    private var sharedPreferences: SharedPreferences? = null
    private val context: Context = ArchApp.getInstance().getContext()


    abstract fun getPreferenceName(): String

    /**
     * 获取setting的sp文件
     *
     * @return
     */
    fun getSharedPreferences(): SharedPreferences {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE)
        }
        return sharedPreferences!!
    }

    fun setSetting(key: String, value: String) {
        val editor = getSharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSetting(key: String): String {
        return getSharedPreferences().getString(key, "")!!
    }

    fun getSetting(key: String, defaultValue: String?): String {
        return getSharedPreferences().getString(key, defaultValue)!!
    }

    fun setSetting(key: String, flag: Boolean) {
        val editor = getSharedPreferences().edit()
        editor.putBoolean(key, flag)
        editor.apply()
    }

    fun getSetting(key: String, defaultValue: Boolean): Boolean {
        return getSharedPreferences().getBoolean(key, defaultValue)
    }

    fun setSetting(key: String, flag: Int) {
        val editor = getSharedPreferences().edit()
        editor.putInt(key, flag)
        editor.apply()
    }

    fun getSetting(key: String, defaultValue: Int): Int {
        return getSharedPreferences().getInt(key, defaultValue)
    }

    fun setSetting(key: String, flag: Long) {
        val editor = getSharedPreferences().edit()
        editor.putLong(key, flag)
        editor.apply()
    }

    fun getSetting(key: String, defaultValue: Long): Long {
        return getSharedPreferences().getLong(key, defaultValue)
    }

    fun setSetting(key: String, array: MutableSet<String>) {
        val editor = getSharedPreferences().edit()
        editor.putStringSet(key, array)
        editor.apply()
    }

    fun getSetting(key: String, array: MutableSet<String>): MutableSet<String> {
        return getSharedPreferences().getStringSet(key, array) ?: mutableSetOf()
    }

    /**
     * 清空用户配置
     */
    fun clear() {
        val editor = getSharedPreferences().edit()
        editor.clear()
        editor.apply()
    }
}