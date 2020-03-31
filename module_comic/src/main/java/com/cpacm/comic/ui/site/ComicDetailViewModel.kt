package com.cpacm.comic.ui.site

import android.content.Context
import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.model.bean.HistoryEvent
import com.cpacm.libarch.model.http.RxBus
import com.cpacm.libarch.mvvm.BaseViewModel

/**
 * <p>
 *
 * @author cpacm 2019-12-06
 */
abstract class ComicDetailViewModel : BaseViewModel() {

    val comic = MutableLiveData<ComicDetailBean>()
    val chapterPos = MutableLiveData<String>()
    var page = -1

    abstract fun getComicDetail(comicId: String)

    init {
        val disposable = RxBus.default!!.toObservable(HistoryEvent::class.java)
                .subscribe({
                    if (it.chapterId != "-1") {
                        chapterPos.postValue(it.chapterId)
                        page = it.chapterPos
                    }
                }, {})
        addDisposable(disposable)
    }

    open fun isReverse(): Boolean {
        return true
    }

    open fun setCategoryLink(context: Context, str: String): SpannableString {
        return SpannableString(str)
    }

    open fun setAuthorLink(context: Context, str: String): SpannableString {
        return SpannableString(str)
    }


}