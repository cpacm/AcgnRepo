package com.cpacm.comic.ui.site

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicMenuBean

/**
 * <p>
 *
 * @author cpacm 2019-12-11
 */
class ComicMenuAdapter(val context: Context) : RecyclerView.Adapter<ComicMenuAdapter.ItemViewHolder>() {

    private var dataList: MutableList<ComicMenuBean> = arrayListOf()
    var menuItemListener: OnMenuItemListener? = null

    fun setData(data: List<ComicMenuBean>) {
        this.dataList.clear()
        this.dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_dialog_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataList.get(position)
        holder.itemTv.text = data.key
        holder.itemTv.setOnClickListener {
            menuItemListener?.onItemClick(position, data.key, data.value)
        }
    }


    interface OnMenuItemListener {
        fun onItemClick(position: Int, key: String, value: String)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTv: TextView

        init {
            itemTv = itemView.findViewById(R.id.item)
        }
    }
}