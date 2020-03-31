package com.cpacm.comic.ui.site

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.bumptech.glide.Glide
import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.ui.search.ComicSearchActivity
import com.cpacm.libarch.ui.AbstractAppActivity
import com.cpacm.liblist.RefreshRecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_comic_list.*

/**
 * <p>
 *     动漫站点详情
 * @author cpacm 2019-12-06
 */
class ComicListActivity : AbstractAppActivity<ComicListViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var comicAdapter: ComicListAdapter
    private var menuDialog: MaterialDialog? = null
    private var menuAdapter: ComicMenuAdapter? = null

    override val viewModelClass: Class<out ComicListViewModel>
        get() = ComicApp.getInstance().comicSite!!.getVMListClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_list)

        val actionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            }
        actionBarDrawerToggle.syncState()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        navigationView.setNavigationItemSelectedListener(this)

        navigationView.inflateMenu(R.menu.comic_drawer_list)

        toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        Glide.with(this).load(viewModel.getLogo()).circleCrop().into(siteLogo)

        siteLogo.setOnClickListener(drawerLayoutListener)
        siteMenu.setOnClickListener(drawerLayoutListener)
        searchIcon.setOnClickListener { ComicSearchActivity.start(this) }

        comicAdapter = ComicListAdapter(this)
        refreshLayout.setAdapter(comicAdapter)
        refreshLayout.setLayoutManager(LinearLayoutManager(this))
        refreshLayout.setRefreshListener(object : RefreshRecyclerView.RefreshListener {
            override fun onSwipeRefresh() {
                viewModel.refresh()
            }

            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })

        menuAdapter = ComicMenuAdapter(this)
        menuDialog = MaterialDialog(this).customListAdapter(menuAdapter!!)
        menuDialog!!.getRecyclerView().layoutManager = GridLayoutManager(this, 3)

        if (!viewModel.needLogin(this)) {
            refreshLayout.startSwipeAfterViewCreate()
        }
    }

    private val drawerLayoutListener = View.OnClickListener {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        } else {
            drawerLayout.openDrawer(navigationView)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_back -> finish()
            R.id.nav_update -> {
                viewModel.setType(ComicListViewModel.ComicDataType.DATA_RECENTLY)
                refreshLayout.startSwipeAfterViewCreate()
                drawerLayout.closeDrawers()
            }
            R.id.nav_rank -> {
                showRankDialog()
            }
            R.id.nav_category -> {
                showCategoryDialog()
            }
            R.id.nav_relogin -> {
                viewModel.needLogin(this, true)
            }
        }
        return true
    }

    private var firstBackPress = 0L
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - firstBackPress > 3000) {
            firstBackPress = currentTime
            Toast.makeText(this, R.string.comic_exit, Toast.LENGTH_SHORT).show()
            return
        }
        super.onBackPressed()
    }

    private fun showRankDialog() {
        drawerLayout.closeDrawers()
        menuAdapter!!.setData(viewModel.getRankMenu())
        menuAdapter!!.menuItemListener = object : ComicMenuAdapter.OnMenuItemListener {
            override fun onItemClick(position: Int, key: String, value: String) {
                viewModel.setTitle(key)
                viewModel.setType(ComicListViewModel.ComicDataType.DATA_RANK, value)
                refreshLayout.startSwipeAfterViewCreate()
                menuDialog?.dismiss()
            }
        }
        menuDialog?.title(R.string.comic_drawer_rank)?.show()
    }

    private fun showCategoryDialog() {
        drawerLayout.closeDrawers()
        menuAdapter!!.setData(viewModel.getCategoryMenu())
        menuAdapter!!.menuItemListener = object : ComicMenuAdapter.OnMenuItemListener {
            override fun onItemClick(position: Int, key: String, value: String) {
                viewModel.setType(ComicListViewModel.ComicDataType.DATA_CATEGORY, value)
                viewModel.setTitle(key)
                refreshLayout.startSwipeAfterViewCreate()
                menuDialog?.dismiss()
            }
        }
        menuDialog?.title(R.string.comic_drawer_category)?.show()

    }


    override fun observeLiveData() {
        viewModel.comicList.observe(this, Observer {
            comicAdapter.setData(it)
            if (it.isEmpty()) {
                refreshLayout.notifyDataSetEmpty({
                    refreshLayout.startSwipeAfterViewCreate()
                }, getString(R.string.comic_empty))
            } else {
                refreshLayout!!.notifyDataSetAll()
            }
        })

        viewModel.hasMore.observe(this, Observer {
            refreshLayout!!.notifyDataSetAll(it)
        })

        viewModel.title.observe(this, Observer {
            comicSiteTitle.text = it
        })

        viewModel.refresh.observe(this, Observer {
            refreshLayout.startSwipeAfterViewCreate()
        })
    }

    override fun onErrorCallback(it: Throwable?) {
        super.onErrorCallback(it)
        refreshLayout.notifyDataSetMoreError {
            if (viewModel.comicList.value?.isEmpty() == true) refreshLayout.startSwipeAfterViewCreate()
            viewModel.loadMore()
        }
    }

}