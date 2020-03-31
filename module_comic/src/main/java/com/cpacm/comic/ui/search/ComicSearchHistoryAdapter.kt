package com.cpacm.comic.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpacm.comic.R
import com.cpacm.comic.model.ComicSettingManager


/**
 * @author: cpacm
 * @date: 2019/12/8
 * @desciption: 搜索记录
 */

class ComicSearchHistoryAdapter(private val context: Context) : RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var history: List<String>? = null
    var historyListener: OnHistoryListener? = null

    init {
        history = ComicSettingManager.getInstance().searchHistory
    }

    fun update() {
        history = ComicSettingManager.getInstance().searchHistory
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        if (viewType == 0) {
            val headerView = LayoutInflater.from(context).inflate(R.layout.recycler_comic_search_history_header, parent, false)
            return HeaderViewHolder(headerView)
        }
        val contentView = LayoutInflater.from(context).inflate(R.layout.recycler_comic_search_history_item, parent, false)
        return HistoryViewHolder(contentView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder) {
            val str = history!![position - 1]
            holder.title.text = str
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }


    override fun getItemCount(): Int {
        return if (history!!.size == 0) 0 else history!!.size + 1
    }

    inner class HeaderViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        val clearTv: TextView

        init {
            clearTv = itemView.findViewById<View>(R.id.history_clear) as TextView
            clearTv.setOnClickListener {
                ComicSettingManager.getInstance().clearSearchHistory()

                update()
                if (historyListener != null) {
                    historyListener!!.clearHistory()
                }
            }
        }
    }

    inner class HistoryViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val title: TextView
        val itemLayout: View

        init {
            itemLayout = itemView.findViewById(R.id.item_layout)
            title = itemView.findViewById<View>(R.id.item_title) as TextView
            itemLayout.setOnClickListener {
                val position = adapterPosition
                val keyword = history!![position - 1]
                if (historyListener != null) {
                    historyListener!!.onHistoryClick(keyword)
                }
            }
        }
    }

    interface OnHistoryListener {
        fun clearHistory()

        fun onHistoryClick(keyword: String)
    }
}
