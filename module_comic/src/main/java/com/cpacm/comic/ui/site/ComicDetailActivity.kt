package com.cpacm.comic.ui.site

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.model.bean.ComicDetailBean
import com.cpacm.comic.ui.search.ComicSearchActivity
import com.cpacm.libarch.ui.widgets.TagGroup
import com.cpacm.libarch.mvvm.BaseActivity
import com.cpacm.libarch.ui.widgets.AppBarStateChangeListener
import com.cpacm.libarch.ui.widgets.ExpandableTextView
import com.cpacm.libarch.utils.BitmapUtils
import com.cpacm.liblist.RefreshRecyclerView
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_comic_detail.*
import kotlinx.android.synthetic.main.activity_comic_detail.contentLayout
import kotlinx.android.synthetic.main.activity_comic_panel.*
import kotlinx.android.synthetic.main.activity_comic_panel.comicCover

/**
 *
 * <p>
 *
 * @author cpacm 2018/2/5
 */
class ComicDetailActivity : BaseActivity<ComicDetailViewModel>(),
    RefreshRecyclerView.RefreshListener {

    private var comicId: String = "0"
    private var appBarOffset = -1
    private lateinit var comicDetailAdapter: ComicDetailAdapter
    private var readButton: View? = null
    private var readTv: TextView? = null
    private var tagGroup: TagGroup? = null
    private var tagLayout: ViewGroup? = null
    private var comicDescription: ExpandableTextView? = null

    private var adContainer: FrameLayout? = null

    companion object {
        fun start(context: Context, comicId: String) {
            val intent = Intent(context, ComicDetailActivity::class.java)
            intent.putExtra("comicid", comicId)
            context.startActivity(intent)
        }
    }


    override val viewModelClass: Class<out ComicDetailViewModel>
        get() = ComicApp.getInstance().comicSite!!.getVMDetailClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        @Suppress("DEPRECATION")
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        initRefreshList()

        val dp20 = BitmapUtils.dp2px(20)
        val margin = BitmapUtils.dp2px(16)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarOffset == verticalOffset) return@OnOffsetChangedListener
            val ratio = Math.abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            appBarOffset = verticalOffset
            comicPanel.alpha = 1 - ratio
            comicPanel.translationY = -(4 + ratio * 0.8f) * dp20

            //collectButton.translationX = -2.2f * marginEnd * ratio
            appBar.elevation = Math.max(0f, 12 * (ratio - 0.95f) / 0.05f)
            val layoutParams = contentLayout?.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.topMargin = ((1 - ratio) * margin).toInt()
            contentLayout?.layoutParams = layoutParams
        })

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_comic_shadow_back)
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State, offset: Float) {
                if (state == State.COLLAPSED) {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_comic_arrow_back)
                } else if (state == State.EXPANDED) {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_comic_shadow_back)
                }
            }
        })

        readButton?.setOnClickListener {
            startComicReader(comicDetailAdapter.getStartPosition(viewModel.isReverse()))
        }

        refreshLayout.startSwipeAfterViewCreate()
    }

    private fun initRefreshList() {

        comicId = intent?.getStringExtra("comicid") ?: "0"

        val header = LayoutInflater.from(this)
            .inflate(R.layout.recycler_comic_detail_header, refreshLayout, false)
        readButton = header.findViewById(R.id.readButton)
        readTv = header.findViewById(R.id.readTv)
        tagGroup = header.findViewById(R.id.tagGroup)
        tagGroup?.setOnTagListener {
            ComicSearchActivity.startKeyword(this@ComicDetailActivity, it, 2)
        }
        tagLayout = header.findViewById(R.id.tagLayout)
        adContainer = header.findViewById(R.id.adContainer)
        comicDescription = header.findViewById(R.id.comicDescription)
        refreshLayout.setHeaderView(header)

        comicDetailAdapter = ComicDetailAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (comicDetailAdapter.getItemViewType(position)) {
                    1 -> return gridLayoutManager.spanCount
                    else -> {
                        return 1
                    }
                }
            }
        }
        refreshLayout.setRefreshListener(this)
        refreshLayout.setLayoutManager(gridLayoutManager)
        refreshLayout.setAdapter(comicDetailAdapter)
        refreshLayout.enableLoadMore(false)
        comicDetailAdapter.chapterClickListener =
            object : ComicDetailAdapter.OnChapterClickListener {
                override fun onChapterClick(position: Int, chapterId: String, title: String) {
                    startComicReader(position)
                }
            }
    }

    override fun onSwipeRefresh() {
        viewModel.getComicDetail(comicId)
    }

    override fun onLoadMore() {
    }


    private fun startComicReader(index: Int) {
        if (index < 0) {
            showToast(R.string.comic_read_error)
            return
        }
        val comic = viewModel.comic.value ?: return
        val isContinue = if (comicDetailAdapter.selectIndex == index) viewModel.page else -1
        showToast(R.string.comic_reader_error)
    }

    override fun observeLiveData() {
        viewModel.comic.observe(this, Observer {
            loadComicData(it)
            refreshLayout.notifyDataSetAll(false)
            refreshLayout.enableSwipeRefresh(false)
        })

        viewModel.chapterPos.observe(this, Observer {
            if (it != "-1") {
                comicDetailAdapter.setChapterId(it) { no ->
                    if (no >= 0) {
                        refreshLayout.notifyDataSetAll()
                    }
                }
                readTv?.setText(R.string.comic_continue_read)
            }
        })
    }

    override fun onErrorCallback(it: Throwable?) {
        showToast(R.string.comic_detail_error)
        refreshLayout.enableSwipeRefresh(true)
        refreshLayout.notifyDataSetAll(false)
    }

    private fun loadComicData(data: ComicDetailBean) {
        supportActionBar?.title = data.title
        comicName.text = viewModel.setAuthorLink(this, data.authors!!)
        comicName.movementMethod = LinkMovementMethod.getInstance()
        comicInfo.text = viewModel.setCategoryLink(this, data.category!!)
        comicInfo.movementMethod = LinkMovementMethod.getInstance()
        comicScore.text = data.hot

        if (!data.tags.isNullOrEmpty()) {
            tagLayout?.visibility = View.VISIBLE
            for (tag in data.tags) {
                tagGroup?.addTag(tag)
            }
        } else {
            tagLayout?.visibility = View.GONE
        }
        //comicScore.movementMethod = LinkMovementMethod.getInstance()

        Glide.with(this)
            .load(data.cover)
            .placeholder(R.drawable.cover_default)
            .transform(RoundedCornersTransformation(BitmapUtils.dp2px(2), 0))
            .into(comicCover)

        Glide.with(this)
            .load(data.cover)
            .placeholder(R.drawable.background_default)
            //.apply(bitmapTransform(BlurTransformation(25)))
            .into(comicBlur)
        comicDescription?.text = data.description

        if (!data.chapters.isNullOrEmpty()) {
            //chapterLayout.visibility = View.VISIBLE
            comicDetailAdapter.setData(data.chapters!!)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}