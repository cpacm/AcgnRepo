package com.cpacm.liblist;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>
 * 添加该监听器，可以在 RecyclerView 滑动时停止加载图片
 *
 * @author cpacm 2017/11/29
 */

public abstract class GlideAutoLoadScrollListener extends RecyclerView.OnScrollListener {

    private Context context;

    public GlideAutoLoadScrollListener(Context context) {
        this.context = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //当屏幕停止滚动，加载图片
                try {
                    if (context != null) resumeRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                try {
                    if (context != null) resumeRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                try {
                    if (context != null) pauseRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public abstract void resumeRequest();

    public abstract void pauseRequest();
}
