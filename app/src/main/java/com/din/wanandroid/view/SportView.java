package com.din.wanandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.din.thedialog.util.Res;

/**
 * @author dinzhenyan
 * @date 2019/4/15 12:47 AM
 * @IDE Android Studio
 */
public class SportView extends View {

    private static final int RING_WIDTH = Res.dp2px(20);
    private static final int RADIUS = Res.dp2px(150);
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HEIGHTLIGHT_COLOR = Color.parseColor("#FF4081");
    private Paint mPaint;
    private String text;
    private Rect mRect;
    private Paint.FontMetrics fontMetrics;
    private float offset;                       // 文字居中需要的偏移量

    public SportView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(Res.dp2px(100));
        mPaint.setTextAlign(Paint.Align.CENTER);
        text = "1000步";

        // 文字纵向居中方法一，在文字不变的情况下都能居中
        mRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        offset = (mRect.top + mRect.bottom) / 2;

        // 文字纵向居中方式二，在文字会变的情况下居中
        fontMetrics = new Paint.FontMetrics();
        mPaint.getFontMetrics(fontMetrics);
        offset = (fontMetrics.ascent + fontMetrics.descent) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制环
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(CIRCLE_COLOR);
        mPaint.setStrokeWidth(RING_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIUS, mPaint);

        // 绘制进度条
        mPaint.setColor(HEIGHTLIGHT_COLOR);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS,
                -90, 225, false, mPaint);

        // 绘制文字
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, getWidth() / 2, getHeight() / 2 - offset, mPaint);
    }
}
