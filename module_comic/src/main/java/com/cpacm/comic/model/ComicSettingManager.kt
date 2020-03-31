package com.cpacm.comic.model

import android.annotation.SuppressLint
import android.text.TextUtils
import com.cpacm.comic.ComicApp
import com.cpacm.libarch.cache.AbstractSettingManager
import java.util.*

/**
 * <p>
 *     漫画配置文件
 * @author cpacm 2019-11-22
 */
class ComicSettingManager private constructor() : AbstractSettingManager() {

    private var allowDoubleTap = true


    override fun getPreferenceName(): String {
        return COMIC_PREFERENCE
    }

    val SEARCH_KEY = "search_history"//搜索历史记录
    val SEARCH_COUNT = 8
    val SEARCH_SPLIT = ",\\^\\$"
    val SEARCH_SPLIT2 = ",^$"

    val searchHistory: MutableList<String>
        get() {
            val searchHistory: MutableList<String>
            val value = getSetting(SEARCH_KEY)
            if (!TextUtils.isEmpty(value)) {
                val array = value.split(SEARCH_SPLIT.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                searchHistory = ArrayList(Arrays.asList(*array))
            } else {
                searchHistory = ArrayList()
            }
            return searchHistory
        }


    fun setSearchHistory(history: String) {
        val searchHistory = searchHistory
        val value = StringBuilder()
        for (str in searchHistory) {
            if (str == history) {
                searchHistory.remove(str)
                break
            }
        }
        searchHistory.add(0, history)
        for (i in searchHistory.indices) {
            value.append(searchHistory[i])
            if (i == searchHistory.size - 1 || i == SEARCH_COUNT - 1) {
                break
            }
            value.append(SEARCH_SPLIT2)
        }
        setSetting(SEARCH_KEY, value.toString())
    }

    fun clearSearchHistory() {
        setSetting(SEARCH_KEY, "")
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var settingInstance: ComicSettingManager? = null

        /**
         * singleton
         *
         * @return
         */
        fun getInstance(): ComicSettingManager {
            if (settingInstance == null) {
                synchronized(ComicSettingManager::class.java) {
                    if (settingInstance == null) {
                        settingInstance = ComicSettingManager()
                    }
                }
            }
            return settingInstance!!
        }

        private const val COMIC_PREFERENCE = "COMIC_MANAGER"//设置文件的文件名

        const val KEYWORD_COMIC_PICA_ADDRESS = "comic_pica_address"
        const val KEYWORD_COMIC_PICA_SERVER = "comic_pica_server"//服务器列表选择，1表示默认，2，3表示镜像
        const val KEYWORD_COMIC_PICA_ADDRESS_SET = "comic_pica_address_set"
        const val KEYWORD_COMIC_AUTHORITY = "comic_login_authority"
        const val KEYWORD_PICA_IMAGE_SERVER = "comic_pica_image_server"
    }
}