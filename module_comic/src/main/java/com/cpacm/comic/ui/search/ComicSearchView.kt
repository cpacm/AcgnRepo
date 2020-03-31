package com.cpacm.comic.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpacm.comic.R
import com.cpacm.comic.model.ComicSettingManager

/**
 * @author: cpacm
 * @pubDate: 2019/12/7
 * @desciption: 搜索框
 */

class ComicSearchView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0) : FrameLayout(context, attributeSet, defAttrStyle) {

    private var searchBackground: View? = null
    private var searchEt: EditText? = null
    private var recentList: RecyclerView? = null
    private var historyAdapter: ComicSearchHistoryAdapter? = null
    private var backIv: ImageView? = null
    private var clearIv: ImageView? = null
    var searchListener: OnSearchListener? = null
    private var hasShown: Boolean = false

    init {
        initSearchView(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.comic_search_view, this, true)
        searchBackground = findViewById(R.id.transparent_view)
        searchEt = findViewById(R.id.search_et)
        recentList = findViewById(R.id.recent_list)
        backIv = findViewById(R.id.back_icon)
        backIv!!.setOnClickListener {
            if (searchListener != null) {
                searchListener!!.onBack()
            }
        }
        clearIv = findViewById(R.id.search_close)
        clearIv!!.setOnClickListener { searchEt!!.setText("") }
        historyAdapter = ComicSearchHistoryAdapter(context)
        historyAdapter!!.historyListener = object : ComicSearchHistoryAdapter.OnHistoryListener {
            override fun clearHistory() {
                closeSearch()
            }

            override fun onHistoryClick(keyword: String) {
                if (searchListener != null) {
                    searchListener!!.onSearch(keyword)
                    searchEt!!.setText(keyword)
                    searchEt!!.setSelection(keyword.length)
                    closeSearch()
                }
            }
        }

        recentList!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recentList!!.adapter = historyAdapter
        searchBackground!!.setOnClickListener { closeSearch() }
        searchEt!!.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == 0 || actionId == 3) && event != null) {
                if (TextUtils.isEmpty(v.text)) {
                    return@setOnEditorActionListener false
                }
                if (searchListener != null) {
                    searchListener!!.onSearch(v.text.toString())
                }
                closeSearch()
                ComicSettingManager.getInstance().setSearchHistory(v.text.toString())
                historyAdapter!!.update()
            }
            false
        }
        searchEt!!.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP && !hasShown) {
                openSearch()
            }
            false
        }

        searchEt!!.requestFocus()
        openSearch()
    }

    fun setText(keyword: String) {
        searchEt?.setText(keyword)
    }

    fun openSearch() {
        hasShown = true
        showKeyboard(searchEt)
        searchBackground!!.visibility = View.VISIBLE
        recentList!!.visibility = View.VISIBLE
        clearIv!!.visibility = View.VISIBLE
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun showKeyboard(view: View?) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view!!.hasFocus()) {
            view.clearFocus()
        }
        view!!.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun closeSearch() {
        hasShown = false
        searchEt!!.clearFocus()
        searchBackground!!.visibility = View.GONE
        recentList!!.visibility = View.GONE
        hideKeyboard(searchEt!!)
        clearIv!!.visibility = View.INVISIBLE
    }

    interface OnSearchListener {
        fun onBack()
        fun onSearch(keyword: String)
    }
}
