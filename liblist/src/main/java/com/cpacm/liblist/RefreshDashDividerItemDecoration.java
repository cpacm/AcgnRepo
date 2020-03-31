package com.cpacm.liblist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>
 * recyclerview 上的统一分隔线
 *
 * @author cpacm 2017/7/28
 */

public class RefreshDashDividerItemDecoration extends RecyclerView.ItemDecoration {

    private boolean drawLastDivider;
    private Paint mPaint;
    private Path mPath;
    private int height = 10;
    private int horizontalPadding;

    public RefreshDashDividerItemDecoration(Context context) {
        this(context, true);
    }

    public RefreshDashDividerItemDecoration(Context context, boolean drawLastDivider) {
        this.drawLastDivider = drawLastDivider;
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#20000000"));
        // 需要加上这句，否则画不出东西
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 4}, 0));
        mPath = new Path();

        horizontalPadding = dp2px(context, 10);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, height);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final int left = parent.getPaddingLeft() + horizontalPadding;
        final int right = parent.getWidth() - parent.getPaddingRight() - horizontalPadding;

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
            int centerY = height / 2;
            mPath.reset();
            mPath.moveTo(left, top + centerY);
            mPath.lineTo(right, top + centerY);
            c.drawPath(mPath, mPaint);
        }
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dp （DisplayMetrics类中属性density）
     * @return
     */
    public int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

}
