package com.din.wanandroid.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class WaveView extends View implements View.OnClickListener {

    private int mViewWidth;
    private int mViewHeight;

    private int mCenterHeight;

    private int mWaveLength;

    // 三层波浪绘制
    private Path mPathOne;
    private Path mPathTwo;
    private Path mPathThree;

    private Paint mPaintOne;
    private Paint mPaintTwo;
    private Paint mPaintThree;

    // 波浪颜色
    private int mColorOne = Color.parseColor("#72c8ff");
    private int mColorTwo = Color.parseColor("#b9e8ff");
    private int mColorThree = Color.parseColor("#997ce8ef");

    private int mGradient;
    private ValueAnimator mValueAnimator;
    private boolean mIsPlaying;
    private float mOffset;

    private boolean mIsTouchable = true;
    private boolean mIsCircle = true;
    private int mRadius;
    private Bitmap mBackGround;
    private Canvas mCanvas;


    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mCenterHeight = h / 2;

        mGradient = 60;
        mWaveLength = 700;
        mRadius = mWaveLength / 2;

        mPathOne = new Path();
        mPaintOne = newPaint(mColorOne);

        mPathTwo = new Path();
        mPaintTwo = newPaint(mColorTwo);

        mPathThree = new Path();
        mPaintThree = newPaint(mColorThree);

        setOnClickListener(this);
        mBackGround = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas();
        mCanvas.setBitmap(mBackGround);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawWave(canvas, mPathOne, mPaintOne, (int) (mOffset + 0), 0,
                mWaveLength, mCenterHeight, mIsCircle);
        drawWave(canvas, mPathTwo, mPaintTwo, (int) (mOffset + 120), -20,
                mWaveLength, mCenterHeight, mIsCircle);
        drawWave(canvas, mPathThree, mPaintThree, (int) (mOffset - 80), -10,
                mWaveLength, mCenterHeight + 30, mIsCircle);

//        mPathCircle.reset();
//        mPathCircle.addCircle(mViewWidth / 2, mViewHeight / 2, mRadius, Path.Direction.CCW);
//        canvas.drawCircle(mBackGround, 0, 0, null);
    }

    /**
     * 绘制波浪
     *
     * @param canvas
     * @param path
     * @param paint
     * @param offsetX
     * @param offsetY
     * @param waveLength
     * @param centerHeight
     * @param isCircle
     */
    private void drawWave(Canvas canvas, Path path, Paint paint, int offsetX, int offsetY,
                          int waveLength, int centerHeight, boolean isCircle) {
        path.reset();
        int mWaveCount = (int) Math.round(mViewWidth / waveLength + 1.5);
        path.moveTo(-waveLength + offsetX, centerHeight);

        for (int i = 0; i < mWaveCount; i++) {
            //绘制全部n型的贝塞尔曲线
            path.quadTo(-waveLength * 3 / 4 + i * waveLength + offsetX,
                    centerHeight - mGradient - offsetY,
                    -waveLength / 2 + i * waveLength + offsetX,
                    centerHeight);
            //绘制全部U型的贝塞尔曲线
            path.quadTo(-waveLength * 1 / 4 + i * waveLength + offsetX,
                    centerHeight + mGradient + offsetY,
                    i * waveLength + offsetX,
                    centerHeight);
        }

        // 将绘制的路径闭合
        path.lineTo(mViewWidth, mViewHeight);
        path.lineTo(0, mViewHeight);
        path.lineTo(-mWaveLength, centerHeight);
        path.close();

        if (isCircle) {
            paint.setXfermode(null);
            canvas.drawPath(path, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(getCircleBitmap(), 0, 0, paint);
//            mPathCircle.op(path, Path.Op.INTERSECT);
//            canvas.drawPath(mPathCircle, paint);
        } else {
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 点击播放波浪动画
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
    }

    public void playAnim() {
        if (!mIsPlaying) {
            mValueAnimator.start();
            mIsPlaying = true;
        }
    }

    public void pauseAnim() {
        if (mIsPlaying) {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.pause();
                mIsPlaying = false;
            }
        }
    }

    private void initValueAnimator() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, mWaveLength);
            mValueAnimator.setDuration(750);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);      // 设置重复次数为一直重复
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset = ((float) animation.getAnimatedValue());
                    postInvalidate();
                }
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mOffset = 0;
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsTouchable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initValueAnimator();
                    if (mIsPlaying) {
                        pauseAnim();
                    } else {
                        playAnim();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCenterHeight = (int) event.getY();
                    invalidate();
                    break;
            }
        }
        return true;
    }

    /**
     * 创建画笔
     *
     * @param color
     * @return
     */
    private Paint newPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    /**
     * 绘制形状
     *
     * @return
     */
    public Bitmap getCircleBitmap() {
        Canvas canvas = new Canvas(mBackGround);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mRadius, paint);
        return mBackGround;
    }
}