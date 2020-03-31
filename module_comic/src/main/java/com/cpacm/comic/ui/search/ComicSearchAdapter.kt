package com.cpacm.comic.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicBean
import com.cpacm.comic.ui.site.ComicDetailActivity

import java.util.ArrayList

/**
 * 搜索适配器
 *
 * @author cpacm 2017/9/5
 */

class ComicSearchAdapter(private val context: Context) : RecyclerView.Adapter<ComicSearchAdapter.ComicItemViewHolder>() {

    private val searchBeen: MutableList<ComicBean>

    init {
        searchBeen = ArrayList()
    }

    fun setData(been: List<ComicBean>) {
        searchBeen.clear()
        searchBeen.addAll(been)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_comic_search_item, parent, false)
        return ComicItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComicItemViewHolder, position: Int) {
        val data = searchBeen[position]
        Glide.with(context).load(data.cover)
                .placeholder(R.drawable.cover_default)
                .into(holder.coverIv)

        holder.comicTitle.text = data.title
        holder.comicStatus.text = data.status
        if (data.category.isNullOrEmpty()) {
            holder.comicCategory.visibility = View.GONE
        } else {
            holder.comicCategory.visibility = View.VISIBLE
            holder.comicCategory.text = data.category
        }
        holder.comicAuthor.text = data.author
        holder.comicStatus.text = data.status

        holder.comicDesc.text = data.description
        holder.itemView.setOnClickListener {
            ComicDetailActivity.start(context, data.id)
        }
    }

    override fun getItemCount(): Int {
        return searchBeen.size
    }

    class ComicItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val coverIv = itemView.findViewById<ImageView>(R.id.comicCover)
        val comicTitle = itemView.findViewById<TextView>(R.id.comicTitle)
        val comicStatus = itemView.findViewById<TextView>(R.id.comicStatus)
        val comicAuthor = itemView.findViewById<TextView>(R.id.comicAuthor)
        val comicCategory = itemView.findViewById<TextView>(R.id.comicCategory)
        val comicDesc = itemView.findViewById<TextView>(R.id.comicDesc)


    }
}
