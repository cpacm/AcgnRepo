package com.cpacm.libarch.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import com.cpacm.libarch.R;
import com.cpacm.libarch.utils.BitmapUtils;

/**
 * <p>
 * 阅读进度条
 *
 * @author cpacm 2017/10/28
 */

public class ReaderSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private Paint textPaint;
    private Drawable drawable;
    private String progress = "0%";

    public ReaderSeekBar(Context context) {
        super(context);
        initView();
    }

    public ReaderSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setThumb(getContext().getResources().getDrawable(R.drawable.progress_thumb));
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getContext().getResources().getColor(R.color.black));
        textPaint.setTextSize(BitmapUtils.INSTANCE.sp2px(8));
    }

    @Override
    public void setThumb(Drawable thumb) {
        Rect localRect = null;
        if (drawable != null) {
            localRect = drawable.getBounds();
        }
        super.setThumb(drawable);
        drawable = thumb;
        if ((localRect != null) && (drawable != null)) {
            drawable.setBounds(localRect);
        }
    }

    @Override
    public Drawable getThumb() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getThumb();
        }
        return drawable;
    }

    public void setProgress(int progress) {
        super.setProgress(progress);
        this.progress = progress * 100 / getMax() + "%";
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float textWidth = textPaint.measureText(progress);
        float left = getPaddingLeft() + getThumb().getBounds().left + (getThumb().getBounds().width() - textWidth) / 2 - getThumbOffset();
        float baseLine = getPaddingTop() + getThumb().getBounds().top + getTextBaseLine();
        canvas.drawText(progress, left, baseLine, textPaint);
        canvas.restore();
    }

    private float getTextBaseLine() {
        return (drawable.getIntrinsicHeight() - (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top)) / 2 - textPaint.getFontMetrics().top - 2;
    }


}
