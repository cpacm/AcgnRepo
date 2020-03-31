package com.cpacm.comic.ui.search

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpacm.comic.ComicApp
import com.cpacm.comic.R

import com.cpacm.libarch.ui.AbstractAppActivity
import com.cpacm.liblist.DividerItemDecoration
import com.cpacm.liblist.RefreshRecyclerView
import kotlinx.android.synthetic.main.activity_comic_search.*

/**
 *
 * 搜索界面
 * @author cpacm 2017/9/4
 */
class ComicSearchActivity : AbstractAppActivity<ComicSearchViewModel>(),
    RefreshRecyclerView.RefreshListener {

    private val searchAdapter by lazy { ComicSearchAdapter(this) }

    override val viewModelClass = ComicApp.getInstance().comicSite!!.getVMSearchClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)

        searchView.searchListener = object : ComicSearchView.OnSearchListener {
            override fun onSearch(keyword: String) {
                viewModel.startSearch(keyword, 0)
                refreshView.setRefreshing(true)
            }

            override fun onBack() {
                onBackPressed()
            }

        }

        refreshView.setAdapter(searchAdapter)
        refreshView.setViewHolder(
            LayoutInflater.from(this).inflate(R.layout.refresh_empty_layout, refreshView, false)
        )
        refreshView.enableLoadMore(true)
        //refreshView.addItemDecoration(DividerItemDecoration(this))

        refreshView.setLayoutManager(LinearLayoutManager(this))
        refreshView.setRefreshListener(this)

        if (intent != null && !intent.getStringExtra("keyword").isNullOrEmpty()) {
            val keyword = intent.getStringExtra("keyword")
            val type = intent.getIntExtra("type", 0)
            viewModel.startSearch(keyword, type)
            refreshView.setRefreshing(true)
            searchView.closeSearch()
        }

    }

    override fun observeLiveData() {
        viewModel.results.observe(this, Observer {
            it?.let {
                searchAdapter.setData(it)
                refreshView.notifyDataSetAll()
            }
        })

        viewModel.hasMore.observe(this, Observer {
            it?.let {
                refreshView.enableLoadMore(it)
            }
        })

        viewModel.keyword.observe(this, Observer {
            searchView.setText(it)
        })
    }

    override fun onErrorCallback(it: Throwable?) {
        super.onErrorCallback(it)
        refreshView.notifyDataSetMoreError {
            viewModel.searchMore()
        }
    }

    override fun onSwipeRefresh() {
        viewModel.search()
    }

    override fun onLoadMore() {
        viewModel.searchMore()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ComicSearchActivity::class.java)
            context.startActivity(intent)
        }

        fun startKeyword(context: Context, keyword: String, type: Int) {
            val intent = Intent(context, ComicSearchActivity::class.java)
            intent.putExtra("keyword", keyword)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }
}
