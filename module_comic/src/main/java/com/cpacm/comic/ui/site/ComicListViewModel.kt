package com.cpacm.comic.ui.site

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.model.bean.ComicMenuBean
import com.cpacm.libarch.mvvm.BaseViewModel

/**
 * <p>
 *
 * @author cpacm 2019-12-06
 */
abstract class ComicListViewModel : BaseViewModel() {

    val comicList = MutableLiveData<MutableList<ComicBean>>()
    val hasMore = MutableLiveData<Boolean>()
    val categoryList = arrayListOf<ComicMenuBean>()
    val title = MutableLiveData<String>()
    val refresh = MutableLiveData<Boolean>()

    var dataType: ComicDataType = ComicDataType.DATA_RECENTLY

    abstract fun setType(type: ComicDataType, extra: String = "0")

    abstract fun refresh()

    abstract fun loadMore()

    abstract fun getRankMenu(): List<ComicMenuBean>

    abstract fun getLogo(): Int

    open fun needLogin(context: Context, reLogin: Boolean = false): Boolean {
        return false
    }

    fun setTitle(title: String) {
        this.title.postValue(title)
    }

    fun getCategoryMenu(): List<ComicMenuBean> {
        return categoryList
    }


    enum class ComicDataType {
        DATA_RECENTLY,
        DATA_RANK,
        DATA_CATEGORY
    }
}