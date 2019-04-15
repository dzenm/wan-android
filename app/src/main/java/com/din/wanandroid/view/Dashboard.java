package com.din.wanandroid.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.din.thedialog.util.Res;

/**
 * @author dinzhenyan
 * @date 2019/4/13 11:28 PM
 * @IDE Android Studio
 */
public class Dashboard extends View {

    private static final int ANGLE = 120;

    private static final int DEFAULT_WIDTH = Res.dp2px(100);
    private static final int DEFAULT_HEIGHT = Res.dp2px(100);

    private static final float STROKE_WIDTH = Res.dp2px(2);

    private Paint mPaintArc;
    private Paint mPaintDash;
    private Paint mPaintPointer;
    private Path mDash;
    private PathDashPathEffect effect;
    private RectF mRectF;
    private int currentAngle = 60;

    public Dashboard(Context context) {
        super(context);
    }

    public Dashboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {   // 可直接被构造方法调用
        mPaintArc = newPaint(STROKE_WIDTH);
        mPaintDash = newPaint(STROKE_WIDTH);
        mPaintPointer = newPaint(STROKE_WIDTH);
        mDash = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, getWidth() - 2 * STROKE_WIDTH, getHeight() - 2 * STROKE_WIDTH);
        mDash.addRect(0, 0, STROKE_WIDTH, STROKE_WIDTH, Path.Direction.CW);

        Path arc = new Path();
        arc.addArc(mRectF, 90 + ANGLE / 2, 360 - ANGLE);

        // 测量弧度的长度(先通过圆弧的path获得PathMeasure，然后在通过PathMeasure获取长度
        // 总共需要显示从0-20的刻度，即共需要21个刻度，需要减去最后一个用于闭合的刻度)
        PathMeasure measure = new PathMeasure(arc, false);
        float advance = (measure.getLength() - STROKE_WIDTH) / 20;
        effect = new PathDashPathEffect(mDash, advance, 0, PathDashPathEffect.Style.ROTATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画线
        canvas.drawArc(mRectF, 90 + ANGLE / 2, 360 - ANGLE, false, mPaintArc);

        // 设置Dash，画刻度
        mPaintDash.setPathEffect(effect);
        canvas.drawArc(mRectF, 90 + ANGLE / 2, 360 - ANGLE, false, mPaintDash);
        mPaintDash.setPathEffect(null);

        int length = (int) getWidth() * 3 / 8;
        // 画指针
        canvas.drawLine(
                getWidth() / 2, getHeight() / 2,
                (float) (getWidth() / 2 * (1 - Math.cos(getAngleFromMark(currentAngle)))),
                (float) (getHeight() / 2 * (1 - Math.sin(getAngleFromMark(currentAngle)))),
                mPaintPointer);

        // 中心点
        Paint mPoint = new Paint();
        mPoint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Res.dp2px(4), mPoint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = measureHeight(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int defaultWidth, int measureSpec) {
        int width = defaultWidth;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            width = DEFAULT_WIDTH;
        } else if (specMode == MeasureSpec.EXACTLY) {
            width = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            width = Math.max(defaultWidth, specSize);
        }
        return width;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {
        int height = defaultHeight;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            height = DEFAULT_HEIGHT;
        } else if (specMode == MeasureSpec.EXACTLY) {
            height = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            height = Math.max(defaultHeight, specSize);
        }
        return height;
    }

    private Paint newPaint(float stroke) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(stroke);
        return mPaint;
    }

    private int getAngleFromMark(int mark) {
        return (int) (90 + (float) ANGLE / 2 + (360 - (float) ANGLE) / 20 + mark);
    }
}
