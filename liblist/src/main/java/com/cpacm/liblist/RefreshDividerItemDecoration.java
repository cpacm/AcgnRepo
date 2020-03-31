package com.cpacm.liblist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * <p>
 * recyclerview 上的统一分隔线
 *
 * @author cpacm 2017/7/28
 */

public class RefreshDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private boolean drawLastDivider;

    public RefreshDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, android.R.drawable.divider_horizontal_bright);
        drawLastDivider = true;
    }

    public RefreshDividerItemDecoration(Context context, boolean drawLastDivider) {
        mDivider = ContextCompat.getDrawable(context, android.R.drawable.divider_horizontal_bright);
        this.drawLastDivider = drawLastDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < (drawLastDivider ? childCount : childCount - 1); i++) {
            final View child = parent.getChildAt(i);
            if (parent.getAdapter().getItemViewType(i) == RefreshRecyclerView.RefreshRecycleAdapter.HEADER
                    || parent.getAdapter().getItemViewType(i) == RefreshRecyclerView.RefreshRecycleAdapter.FOOTER) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
