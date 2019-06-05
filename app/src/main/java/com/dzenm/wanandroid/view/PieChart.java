package com.dzenm.wanandroid.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

public class PieChart extends View {

    private static final int RADIUS = dp2px(100);
    private static final int LENGTH = dp2px(12);

    private static final int PULL_OUT_INDEX = 2;
    private Paint mPaint;
    private RectF mBounds = new RectF();
    private int[] angles = {60, 100, 120, 80};
    private int[] colors = {Color.parseColor("#2979FF"), Color.parseColor("#C2185B"),
            Color.parseColor("#009688"), Color.parseColor("#FF8F00")};

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int currentAngle = 0;
        for (int i = 0; i < angles.length; i++) {
            mPaint.setColor(colors[i]);
            canvas.save();
            if (i == PULL_OUT_INDEX) {
                canvas.translate((float) Math.cos(Math.toRadians(currentAngle + angles[i] / 2)) * LENGTH,
                        (float) Math.cos(Math.toRadians(currentAngle + angles[i] / 2)) * LENGTH);
            }
            canvas.drawArc(mBounds, currentAngle, angles[i], true, mPaint);
            canvas.restore();
            currentAngle += angles[i];
        }
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
