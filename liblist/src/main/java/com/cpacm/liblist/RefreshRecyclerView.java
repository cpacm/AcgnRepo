package com.cpacm.liblist;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>
 * 可上拉下拉加载的RecycleView
 *
 * @author cpacm 2016/7/11
 */
public class RefreshRecyclerView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private boolean isLoadingMore = false;
    private RefreshListener refreshListener;
    private RefreshRecycleAdapter refreshRecycleAdapter;
    private View viewHolder;
    private ViewGroup refreshLayout;


    private boolean isLoadingMoreEnable = true;
    private boolean isHeaderEnable;
    private boolean isFooterEnable;
    private View loadView;
    private View loadingLayout;
    private TextView reloadView, refreshBtn;
    private View headerView;
    private View footerView;
    private boolean isReloadVisible = true;
    private int showFooterLimit;

    public RefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        this.mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        isHeaderEnable = false;
        isFooterEnable = false;
        View parentView = LayoutInflater.from(context).inflate(R.layout.refresh_recycleview_layout, null, false);
        refreshLayout = parentView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout = parentView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getColorAccent());
        recyclerView = parentView.findViewById(R.id.recycle_view);
        //recyclerView.addItemDecoration(new DividerItemDecoration(context));
        setLayoutManager(new LinearLayoutManager(context));
        loadView = LayoutInflater.from(context).inflate(R.layout.refresh_loadmore_layout, this, false);
        loadingLayout = loadView.findViewById(R.id.loadingLayout);
        reloadView = loadView.findViewById(R.id.loadMoreRefresh);

        viewHolder = LayoutInflater.from(context).inflate(R.layout.refresh_emptyorfail_layout, null, false);
        refreshBtn = viewHolder.findViewById(R.id.refresh_button);
        refreshBtn.setVisibility(isReloadVisible ? View.VISIBLE : View.GONE);
        refreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startSwipeAfterViewCreate();
            }
        });

        footerView = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, this, false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != refreshListener && isLoadingMoreEnable && !isLoadingMore && dy > 0 && loadingLayout.getVisibility() == View.VISIBLE) {
                    int lastVisiblePosition = getLastPosition();
                    if (lastVisiblePosition + 1 == refreshRecycleAdapter.getItemCount()) {
                        setLoadingMore(true);
                        refreshListener.onLoadMore();
                    }
                }
            }
        });
        //recyclerView.addOnScrollListener(new GlideAutoLoadScrollListener(getContext()));
        if (recyclerView.getItemAnimator() != null && recyclerView.getItemAnimator() instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        addView(parentView);
    }

    private int getColorAccent() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public void setReloadVisible(boolean reloadVisible) {
        isReloadVisible = reloadVisible;
        if (recyclerView != null) {
            reloadView.setVisibility(isReloadVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 添加滑动监听器
     *
     * @param listener 例如 GlideAutoLoadScrollListener
     */
    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }


    /**
     * 设置占位视图
     */
    public void setViewHolder(View view) {
        if (view.getParent() != null) {
            throw new IllegalArgumentException("view shouldn't has a parent view");
        }
        if (refreshLayout.indexOfChild(viewHolder) != -1) {
            refreshLayout.removeView(viewHolder);
            viewHolder = view;
            refreshLayout.addView(viewHolder);
        } else {
            viewHolder = view;
        }
    }

    public void enableSwipeRefresh(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 添加列表项目间的装饰器
     *
     * @param itemDecoration 自定义的列表装饰器
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (recyclerView != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (recyclerView != null) {
            recyclerView.removeItemDecoration(itemDecoration);
        }
    }

    @Override
    public void onRefresh() {
        if (null != refreshListener)
            refreshListener.onSwipeRefresh();
    }

    public void startSwipeAfterViewCreate() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 500);
        recyclerView.scrollToPosition(0);
    }

    public void setRefreshing(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (refreshRecycleAdapter == null) {
            refreshRecycleAdapter = new RefreshRecycleAdapter(adapter);
            recyclerView.setAdapter(refreshRecycleAdapter);
        } else
            refreshRecycleAdapter.setInternalAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldssl = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (refreshRecycleAdapter.getItemViewType(position)) {
                        case RefreshRecycleAdapter.HEADER:
                            return gridLayoutManager.getSpanCount();
                        case RefreshRecycleAdapter.LOADMORE:
                            return gridLayoutManager.getSpanCount();
                        case RefreshRecycleAdapter.FOOTER:
                            return gridLayoutManager.getSpanCount();
                        default:
                            if (oldssl == null) return 1;
                            return oldssl.getSpanSize(refreshRecycleAdapter.transPosition(position));
                    }
                }
            });
        }
    }

    public int getLastPosition() {
        int lastPosition;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPoasitions = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            lastPosition = getMaxPosition(lastPoasitions);
        } else {
            lastPosition = (layoutManager != null ? layoutManager.getItemCount() : 0) - 1;
        }
        return lastPosition;
    }

    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    private void setLoadingMore(boolean loadingMore) {
        this.isLoadingMore = loadingMore;
    }

    /**
     * 刷新列表
     *
     * @param hasMore 是否需要加载更多
     */
    public void notifyDataSetAll(boolean hasMore) {
        setLoadingMore(false);
        enableLoadMore(hasMore);
        notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        toggleListOrHolder();
    }

    /**
     * 当加载更多数据失败时调用，如果有加载更多时变为重新加载
     */
    public void notifyDataSetMoreError(final OnReloadCallback callable) {
        isLoadingMore = false;
        swipeRefreshLayout.setRefreshing(false);
        notifyDataSetAll();
        if (showLoadingMoreEnable()) {
            loadingLayout.setVisibility(View.GONE);
            reloadView.setVisibility(View.VISIBLE);
            reloadView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingLayout.setVisibility(View.VISIBLE);
                    reloadView.setVisibility(View.GONE);
                    callable.reload();
                }
            });
        }
    }

    /**
     * 当加载数据失败时调用，如果有加载更多时变为重新加载
     */
    public void notifyDataSetEmpty(final OnReloadCallback callable) {
        isLoadingMore = false;
        swipeRefreshLayout.setRefreshing(false);
        notifyDataSetAll();
        refreshBtn.setText(R.string.refresh_empty);
        refreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callable.reload();
            }
        });
    }

    public void notifyDataSetEmpty(final OnReloadCallback callable, String reloadStr) {
        isLoadingMore = false;
        swipeRefreshLayout.setRefreshing(false);
        notifyDataSetAll();
        refreshBtn.setText(reloadStr);
        refreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callable.reload();
            }
        });
    }

    /**
     * 刷新列表
     */
    public void notifyDataSetAll() {
        isLoadingMore = false;
        notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        toggleListOrHolder();
    }

    private void notifyDataSetChanged() {
        refreshRecycleAdapter.notifyDataSetChanged();
        isLoadingMore = false;
    }

    public void notifyItemDataChanged(int position) {
        refreshRecycleAdapter.notifyItemDataChanged(position);
    }

    private void toggleListOrHolder() {
        if (refreshRecycleAdapter.getItemCount() > 0) {
            if (refreshLayout.indexOfChild(viewHolder) != -1) {
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                refreshLayout.removeView(viewHolder);
                refreshLayout.addView(recyclerView, layoutParams);
            }
        } else {
            if (refreshLayout.indexOfChild(recyclerView) != -1) {
                ViewGroup.LayoutParams layoutParams = viewHolder.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    viewHolder.setLayoutParams(layoutParams);
                }
                refreshLayout.removeView(recyclerView);
                refreshLayout.addView(viewHolder);
            }
        }
    }

    public void enableLoadMore(boolean loadEnable) {
        isLoadingMoreEnable = loadEnable;
    }

    public boolean getLoadingMoreEnable() {
        return isLoadingMoreEnable;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        setHeaderEnable(true);
    }

    public void setHeaderView(int resId) {
        headerView = LayoutInflater.from(mContext).inflate(resId, null);
        setHeaderEnable(true);
    }

    public void setHeaderEnable(boolean headerEnable) {
        this.isHeaderEnable = headerEnable;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        setFooterEnable(true);
    }

    public void setFooterView(int resId) {
        footerView = LayoutInflater.from(mContext).inflate(resId, null);
        setFooterEnable(true);
    }

    public void setFooterEnable(boolean footerEnable) {
        this.isFooterEnable = footerEnable;
    }

    /**
     * 设置列表尾页出现的最少个数
     *
     * @param limit 至少
     */
    public void setShowFooterAtLestCount(int limit) {
        this.isFooterEnable = true;
        this.showFooterLimit = limit;
    }

    private boolean showFooterEnable() {
        if (!isFooterEnable || footerView == null || showLoadingMoreEnable()) return false;
        if (refreshRecycleAdapter.getInternalAdapter() == null) {
            return false;
        }
        return refreshRecycleAdapter.getInternalAdapter().getItemCount() >= showFooterLimit;
    }

    private boolean showHeaderEnable() {
        if (!isHeaderEnable || headerView == null) return false;
        return refreshRecycleAdapter.getInternalAdapter() != null;
    }

    private boolean showLoadingMoreEnable() {
        if (!isLoadingMoreEnable || loadView == null) return false;
        return refreshRecycleAdapter.getInternalAdapter() != null;
    }

    public interface RefreshListener {
        void onSwipeRefresh();

        void onLoadMore();
    }

    public class RefreshRecycleAdapter extends RecyclerView.Adapter {

        public final static int HEADER = 9000;
        public final static int LOADMORE = 9001;
        public final static int NORMAL = 9002;
        public final static int FOOTER = 9003;

        private RecyclerView.Adapter internalAdapter;

        RefreshRecycleAdapter(RecyclerView.Adapter internalAdapter) {
            this.internalAdapter = internalAdapter;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER:
                    return new HeaderViewHolder(headerView);
                case FOOTER:
                    return new FooterViewHolder(footerView);
                case LOADMORE:
                    return new LoadMoreViewHolder(loadView);
                default:
                    return internalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                return;
            }
            if (holder instanceof FooterViewHolder) {
                return;
            }
            if (holder instanceof LoadMoreViewHolder)
                return;
            int realPosition = position;
            if (isHeaderEnable && headerView != null)
                realPosition = realPosition - 1;
            internalAdapter.onBindViewHolder(holder, realPosition);
        }

        /**
         * 转化为 internalAdapter 的 position
         *
         * @param position 最外层的位置
         * @return -1 表示为头部，-2表示为尾部
         */
        public int transPosition(int position) {
            if (position == 0 && isHeaderEnable && headerView != null)
                return -1;
            if (position == getItemCount() - 1 && isLoadingMore)
                return -2;
            if (isHeaderEnable && headerView != null) {
                return position - 1;
            } else return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && showHeaderEnable()) {
                return HEADER;
            }
            if (showLoadingMoreEnable() && position == getItemCount() - 1) {
                return LOADMORE;
            }
            if (showFooterEnable() && position == getItemCount() - 1) {
                return FOOTER;
            }

            if (showHeaderEnable()) {
                return internalAdapter.getItemViewType(position - 1);
            } else return internalAdapter.getItemViewType(position);

         /*
            if (isHeaderEnable && headerView != null) {
                return internalAdapter.getItemViewType(position - 1);
            } else return internalAdapter.getItemViewType(position);*/
        }

        @Override
        public int getItemCount() {
            int count = internalAdapter.getItemCount();
            if (showHeaderEnable()) {
                count += 1;
            }
            if (showLoadingMoreEnable() && count != 0) {
                count += 1;
            }
            if (showFooterEnable()) {
                count += 1;
            }
            return count;
        }

        void notifyItemDataChanged(int position) {
            if (isHeaderEnable && headerView != null) {
                position = position + 1;
            }
            notifyItemChanged(position);
        }

        RecyclerView.Adapter getInternalAdapter() {
            return internalAdapter;
        }

        void setInternalAdapter(RecyclerView.Adapter internalAdapter) {
            this.internalAdapter = internalAdapter;
        }

        class LoadMoreViewHolder extends RecyclerView.ViewHolder {
            LoadMoreViewHolder(View itemView) {
                super(itemView);
            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        class FooterViewHolder extends RecyclerView.ViewHolder {
            FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

    public RefreshRecycleAdapter getRefreshRecycleAdapter() {
        return refreshRecycleAdapter;
    }

    public void smoothScrollToFirst() {
        recyclerView.smoothScrollToPosition(0);
    }

    public void scrollToFirst() {
        recyclerView.scrollToPosition(0);
    }

    public ViewGroup getContentView() {
        return refreshLayout;
    }
}