package com.cpacm.libarch.ui.widgets;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.libarch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cpacm
 * @date 2017/3/2
 * @desciption 标签控件
 */

public class TagGroup extends ViewGroup {

    private Context context;

    private List<TagView> tagList;
    private int lineMargin, tagMargin;
    private int limit = Integer.MAX_VALUE;
    private OnTagListener onTagListener;


    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        tagList = new ArrayList<>();
        lineMargin = dp2px(8);
        tagMargin = dp2px(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int x = 0, y = 0;
        int tagWidth = 0;
        int tagHeight = 0;
        for (TagView view : tagList) {
            tagWidth = view.getMeasuredWidth();
            tagHeight = view.getMeasuredHeight();
            if (x + tagWidth > width) {
                x = 0;
                y = y + tagHeight + lineMargin;
            }
            x = x + tagWidth + tagMargin;
        }
        y += tagHeight;

        setMeasuredDimension(widthMeasureSpec, y);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = 0;
        int y = 0;
        for (TagView view : tagList) {
            int tagWidth = view.getMeasuredWidth();
            int tagHeight = view.getMeasuredHeight();
            if (x + tagWidth > r - l) {
                x = 0;
                y = y + tagHeight + lineMargin;
            }
            view.layout(x, y, x + tagWidth, y + tagHeight);
            x = x + tagWidth + tagMargin;
        }
    }

    public void clearTags() {
        removeAllViews();
        tagList.clear();
    }

    public void addTag(String text) {
        if (tagList.size() >= limit) {
            TagView view = tagList.get(0);
            tagList.remove(0);
            view.setText(text);
            tagList.add(view);
        } else {
            TagView tagView = new TagView(context);
            tagView.setText(text);
            addView(tagView);
            tagList.add(tagView);
        }
        postInvalidate();
    }

    public void addHistoryTag(String text) {
        int size = tagList.size();
        if (size >= limit) {
            TagView tagView = tagList.get(size - 1);
            tagList.remove(tagView);
            tagView.setText(text);
            tagList.add(0, tagView);
        } else {
            TagView tagView = new TagView(context);
            tagView.setText(text);
            addView(tagView, 0);
            tagList.add(0, tagView);
        }
        postInvalidate();
    }

    public void setLimit(int limit) {
        this.limit = limit;
        while (tagList.size() > limit) {
            tagList.remove(tagList.size() - 1);
        }
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dp （DisplayMetrics类中属性density）
     * @return
     */
    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void setOnTagListener(OnTagListener onTagListener) {
        this.onTagListener = onTagListener;
    }

    /**
     * 标签控件
     */
    public class TagView extends AppCompatTextView {

        public TagView(Context context) {
            this(context, null);
        }

        public TagView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView() {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            setTextColor(getResources().getColor(R.color.white_normal));
            setPadding(dp2px(8), dp2px(2), dp2px(8), dp2px(2));
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagListener != null) {
                        onTagListener.onTagClick(getText().toString().trim());
                    }
                }
            });
            setBackground(getResources().getDrawable(R.drawable.common_button_selector));
        }
    }

    public interface OnTagListener {
        void onTagClick(String text);
    }
}
