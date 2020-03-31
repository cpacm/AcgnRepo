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
import com.cpacm.comic.site.ComicSiteFactory

/**
 * <p>
 *
 * @author cpacm 2019-12-10
 */
class ComicSiteAdapter(private val context: Context) :
    RecyclerView.Adapter<ComicSiteAdapter.ComicSiteViewHolder>() {

    private val comicList = arrayListOf<ComicSiteFactory>()
    var comicSiteListener: OnComicSiteListener? = null

    fun setData(data: List<ComicSiteFactory>) {
        comicList.clear()
        comicList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicSiteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_site_item, parent, false)
        return ComicSiteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comicList.size
    }

    override fun onBindViewHolder(holder: ComicSiteViewHolder, position: Int) {
        val data = comicList[position].getComicSiteBean()
        Glide.with(context).load(data.cover)
            .into(holder.coverIv)

        Glide.with(context).load(data.logo).circleCrop()
            .into(holder.logoIv)
        holder.siteTitle.setText(data.title)
        holder.siteEnter.isClickable = false
        holder.itemView.setOnClickListener {
            comicSiteListener?.onComicSiteClick(comicList[position])
        }
    }

    class ComicSiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverIv = itemView.findViewById<ImageView>(R.id.siteCover)
        val logoIv = itemView.findViewById<ImageView>(R.id.siteLogo)
        val siteTitle = itemView.findViewById<TextView>(R.id.siteTitle)
        val siteEnter = itemView.findViewById<TextView>(R.id.siteEnter)
    }

    interface OnComicSiteListener {
        fun onComicSiteClick(site: ComicSiteFactory)
    }
}