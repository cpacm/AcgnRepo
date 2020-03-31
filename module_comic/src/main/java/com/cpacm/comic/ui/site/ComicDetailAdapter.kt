package com.cpacm.comic.ui.site

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicChapterBean
import com.cpacm.comic.model.bean.ComicVolumeBean
import com.cpacm.comic.model.bean.DmzjChapterBean
import com.cpacm.comic.model.bean.DmzjVolumeBean
import com.google.android.material.snackbar.Snackbar
import java.util.stream.IntStream.range

/**
 * <p>
 *     卷章适配器
 * @author cpacm 2019-12-11
 */
class ComicDetailAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList: MutableList<ComicChapterBean> = arrayListOf()
    var chapterClickListener: OnChapterClickListener? = null
    var selectIndex = -1

    fun setData(data: List<ComicVolumeBean>?) {
        if (data.isNullOrEmpty()) return
        this.dataList.clear()
        for (volume in data) {
            if (!volume.content.isNullOrEmpty()) {
                if (!volume.title.isNullOrEmpty()) {
                    val chapter = ComicChapterBean()
                    chapter.chapterTitle = volume.title
                    chapter.isVolume = true
                    this.dataList.add(chapter)
                }
                this.dataList.addAll(volume.content ?: arrayListOf())
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(context).inflate(R.layout.recycler_comic_chapter, parent, false)
            ChapterViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.recycler_comic_volume, parent, false)
            VolumeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chapterBean = dataList[position]
        if (holder is VolumeViewHolder) {
            holder.volumnTv.text = chapterBean.chapterTitle
        } else if (holder is ChapterViewHolder) {
            holder.item.isSelected = selectIndex == position
            holder.chapterTv.text = chapterBean.chapterTitle
            holder.itemView.setOnClickListener {
                chapterClickListener?.onChapterClick(position, chapterBean.chapterId!!, chapterBean.chapterTitle
                        ?: "")
            }
            holder.item.setOnLongClickListener {
                Snackbar.make(it, chapterBean.chapterTitle.toString(), Snackbar.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chapter = dataList[position]
        return if (chapter.isVolume) 1 else 0
    }

    interface OnChapterClickListener {
        fun onChapterClick(position: Int, chapterId: String, title: String)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 获取可以阅读的章节目录
     */
    fun getStartPosition(isReverse: Boolean): Int {
        if (selectIndex != -1) return selectIndex
        if (!isReverse) {
            for (i in 0 until dataList.size) {
                if (!dataList[i].isVolume) {
                    return i
                }
            }
        } else {
            for (i in dataList.size - 1 downTo 0) {
                if (!dataList[i].isVolume) {
                    return i
                }
            }
        }
        return -1
    }

    fun setChapterId(chapterId: String, change: (pos: Int) -> Unit) {
        for (i in 0 until dataList.size) {
            if (chapterId == dataList[i].chapterId) {
                change(selectIndex)
                selectIndex = i
                change(selectIndex)
                return
            }
        }
    }

    fun getData(): ArrayList<ComicChapterBean> {
        return dataList as ArrayList<ComicChapterBean>
    }

    class VolumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var volumnTv: TextView

        init {
            volumnTv = itemView.findViewById(R.id.volumeTv)
        }
    }

    class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var chapterTv: TextView
        var item: ViewGroup

        init {
            chapterTv = itemView.findViewById(R.id.chapterTv)
            item = itemView.findViewById(R.id.item)
        }
    }
}