package com.cpacm.comic.ui.site

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

/**
 * <p>
 *
 * @author cpacm 2019-12-10
 */
class ComicListAdapter(private val context: Context) : RecyclerView.Adapter<ComicListAdapter.ComicItemViewHolder>() {

    private val comicList = arrayListOf<ComicBean>()

    fun setData(data: List<ComicBean>) {
        comicList.clear()
        comicList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_comic_list_item, parent, false)
        return ComicItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comicList.size
    }

    override fun onBindViewHolder(holder: ComicItemViewHolder, position: Int) {
        val data = comicList[position]
        Glide.with(context).load(data.cover)
                .placeholder(R.drawable.cover_default)
                .into(holder.coverIv)
        holder.comicTitle.text = data.title
        holder.comicStatus.text = data.status
        if(data.author.isNullOrEmpty()){
            holder.comicAuthor.visibility = View.GONE
        }else{
            holder.comicAuthor.visibility = View.VISIBLE
            holder.comicAuthor.text = data.author
        }
        holder.comicDesc.text = data.description
        if (data.category.isNullOrEmpty()) {
            holder.comicCategory.visibility = View.GONE
        } else {
            holder.comicCategory.visibility = View.VISIBLE
            holder.comicCategory.text = data.category
        }
        holder.itemView.setOnClickListener {
            ComicDetailActivity.start(context, data.id)
        }
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