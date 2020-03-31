package com.cpacm.libarch.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.cpacm.libarch.R;

/**
 * <p>
 * 黄金比例伸缩卡片式界面
 *
 * @author cpacm 2018/1/10
 */

public class ScaleRatioCardView extends CardView {

    //private final static float GOLDENRADIO = 0.8f;
    private float goldenRadio;
    private float zoomRadio;
    private boolean enableZoom;
    private ValueAnimator zoomInAnimator, zoomOutAnimator;
    private float zoomValue = 1;
    private float elevation = 0;

    public ScaleRatioCardView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public ScaleRatioCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ScaleRatioCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleRatioCardView);

        goldenRadio = typeArray.getFloat(R.styleable.ScaleRatioCardView_radio, 9 / 16.0f);
        zoomRadio = typeArray.getFloat(R.styleable.ScaleRatioCardView_zoom, 0.95f);
        enableZoom = typeArray.getBoolean(R.styleable.ScaleRatioCardView_enableZoom, true);
        typeArray.recycle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = getElevation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (goldenRadio != 0) {
            // 使宽高保持黄金比例
            int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSpec * goldenRadio), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enableZoom) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startZoomInAnimation();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startZoomOutAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        super.onTouchEvent(event);
        return true;
    }


    private void startZoomInAnimation() {
        if (zoomInAnimator == null) {
            zoomInAnimator = new ValueAnimator();
        }
        zoomInAnimator.cancel();
        zoomInAnimator.setFloatValues(zoomValue, zoomRadio);
        zoomInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                zoomValue = (float) animation.getAnimatedValue();
                ScaleRatioCardView.this.setScaleX(zoomValue);
                ScaleRatioCardView.this.setScaleY(zoomValue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ScaleRatioCardView.this.setElevation(elevation * zoomValue);
                }
            }
        });
        zoomInAnimator.setDuration(300);
        zoomInAnimator.setInterpolator(new DecelerateInterpolator());
        zoomInAnimator.start();
    }

    private void startZoomOutAnimation() {
        if (zoomOutAnimator == null) {
            zoomOutAnimator = new ValueAnimator();
        }
        zoomOutAnimator.cancel();
        zoomOutAnimator.setFloatValues(zoomValue, 1f);
        zoomOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                zoomValue = (float) animation.getAnimatedValue();
                ScaleRatioCardView.this.setScaleX(zoomValue);
                ScaleRatioCardView.this.setScaleY(zoomValue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ScaleRatioCardView.this.setElevation(elevation * zoomValue);
                }
            }
        });
        zoomOutAnimator.setDuration(300);
        zoomOutAnimator.setInterpolator(new DecelerateInterpolator());
        zoomOutAnimator.start();
    }
}
