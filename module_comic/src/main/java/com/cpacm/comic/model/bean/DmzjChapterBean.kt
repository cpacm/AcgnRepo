package com.cpacm.comic.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *
 *
 * 动漫之家章节数据
 *
 * @author cpacm 2019-12-03
 */
class DmzjChapterBean() :Parcelable {
    /**
     * chapter_id : 84702
     * chapter_title : 2卷
     * updatetime : 1559878482
     * filesize : 22570027
     * chapter_order : 2
     */
    var chapter_id: Int = 0
    var chapter_title: String? = null
    var updatetime: Int = 0
    var filesize: Int = 0
    var chapter_order: Int = 0

    var isVolume = false

    fun parse():ComicChapterBean{
        val chapter = ComicChapterBean()
        chapter.chapterId = chapter_id.toString()
        chapter.chapterTitle = chapter_title
        chapter.isVolume = isVolume
        return chapter
    }

    constructor(parcel: Parcel) : this() {
        chapter_id = parcel.readInt()
        chapter_title = parcel.readString()
        updatetime = parcel.readInt()
        filesize = parcel.readInt()
        chapter_order = parcel.readInt()
        isVolume = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(chapter_id)
        parcel.writeString(chapter_title)
        parcel.writeInt(updatetime)
        parcel.writeInt(filesize)
        parcel.writeInt(chapter_order)
        parcel.writeByte(if (isVolume) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DmzjChapterBean> {
        override fun createFromParcel(parcel: Parcel): DmzjChapterBean {
            return DmzjChapterBean(parcel)
        }

        override fun newArray(size: Int): Array<DmzjChapterBean?> {
            return arrayOfNulls(size)
        }
    }
}