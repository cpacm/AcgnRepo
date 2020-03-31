package com.cpacm.comic.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *
 *
 * 章节数据
 *
 * @author cpacm 2019-12-03
 */
class ComicChapterBean() : Parcelable {
    /**
     * chapter_id : 84702
     * chapter_title : 2卷
     * updatetime : 1559878482
     * filesize : 22570027
     * chapter_order : 2
     */
    var chapterId: String? = "-1"
    var chapterTitle: String? = null
    var url: String = ""

    var isVolume = false

    constructor(parcel: Parcel) : this() {
        chapterId = parcel.readString()
        chapterTitle = parcel.readString()
        url = parcel.readString()!!
        isVolume = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chapterId)
        parcel.writeString(chapterTitle)
        parcel.writeString(url)
        parcel.writeByte(if (isVolume) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComicChapterBean> {
        override fun createFromParcel(parcel: Parcel): ComicChapterBean {
            return ComicChapterBean(parcel)
        }

        override fun newArray(size: Int): Array<ComicChapterBean?> {
            return arrayOfNulls(size)
        }
    }


}