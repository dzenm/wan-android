package com.din.wanandroid.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dinzhenyan
 * @date 2019/4/13 12:15 AM
 * @IDE Android Studio
 */
public class LoadPointView extends View implements ValueAnimator.AnimatorUpdateListener {

    private List<Paint> mPaints = new ArrayList<>();
    private List<ValueAnimator> valueAnimators = new ArrayList<>();
    private int[] colors;
    private int mRadius = dp2dip(5);
    private int mPointCy = dp2dip(5);

    public LoadPointView(Context context) {
        this(context, null);
    }

    public LoadPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
        for (int i = 0; i < colors.length; i++) {
            mPaints.add(new Paint());
        }
    }

    public void setPointColors(int[] colors) {
        this.colors = colors;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < colors.length; i++) {
            Paint mPaint = mPaints.get(i);
            mPaint.setColor(colors[i]);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            int cx = (3 * i + 1) * mRadius;
            canvas.drawCircle(cx, mPointCy, mRadius, mPaint);
        }
    }


    private ValueAnimator newValueAnimator() {
        ValueAnimator anim = ObjectAnimator.ofFloat(1.0f);

        anim.addUpdateListener(this);
        anim.setDuration(200);
        return anim;
    }

    /**
     * 转化为dip值
     * @param value
     * @return
     */
    private int dp2dip(int value) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {

    }
}