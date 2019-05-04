package com.din.helper.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import com.din.helper.R;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 自定义的进度条加载进度
 */
public class LoadProgress extends View {

    /*
     * 进度条的总高度，即整个View的高度（可通过xml的属性设置）
     */
    private int mTotalHeight;

    /*
     * 进度条显示的高度（可通过xml的属性设置）
     */
    private int mLineHeight;

    /*
     * 已加载的进度条的颜色（可通过xml的属性设置）
     */
    private int mLineColor;

    /*
     * 当前已完成的进度值
     */
    private int mCurrentValue;

    /*
     * 总的进度数量（可通过xml的属性设置）
     */
    private int mMaxValue;

    /*
     * 百分比的文字大小（可通过xml的属性设置）
     */
    private int mTextSize;

    /*
     * 文字区域的宽度
     */
    private int mTextWidth;

    /*
     * 显示的默认文字
     */
    private String mText;

    /*
     * 百分比文字是否静止在末尾（可通过xml的属性设置）
     */
    private boolean isTextStatic;

    /*
     * 加载监听事件
     */
    private OnLoadListener onLoadListener;

    /*
     * 绘制背景灰色线条画笔
     */
    private Paint mPaintLineBackground;

    /*
     * 绘制进度条画笔
     */
    private Paint mPaintLine;

    /*
     * 绘制文字背景画笔
     */
    private Paint mPaintTextBackground;

    /*
     * 绘制下载进度画笔
     */
    private Paint mPaintText;

    /*
     * 获取百分比数字的长宽
     */
    private Rect mBound;

    /*
     * 文字与文字区域的padding值
     */
    private float paddingLeft, paddingRight;

    public LoadProgress(Context context) {
        this(context, null);
    }

    public LoadProgress(Context context, AttributeSet attribute) {
        this(context, attribute, 0);
    }

    public LoadProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.LoadProgress);     // 获取自定义属性
        mTotalHeight = (int) t.getDimension(R.styleable.LoadProgress_totalHeight, dip(40));
        mLineHeight = (int) t.getDimension(R.styleable.LoadProgress_lineHeight, dip(4));
        mTextSize = (int) t.getDimension(R.styleable.LoadProgress_textSize, 36);
        mMaxValue = t.getInteger(R.styleable.LoadProgress_maxValue, 100);
        mLineColor = t.getColor(R.styleable.LoadProgress_lineColor, getResources().getColor(android.R.color.holo_blue_light));
        isTextStatic = t.getBoolean(R.styleable.LoadProgress_textStatic, false);
        mCurrentValue = 0;
        mText = "100%";
        mTextWidth = getTextWidth();
    }

    {
        mPaintLineBackground = new Paint();
        mPaintLine = new Paint();
        mPaintTextBackground = new Paint();
        mPaintText = new Paint();
        mBound = new Rect();
        paddingLeft = dip(2);
        paddingRight = dip(2);
    }

    /**
     * 当前进度值
     * @param currentValue
     */
    public LoadProgress setCurrentValue(int currentValue) {
        mCurrentValue = currentValue;
        invalidate();
        return this;
    }

    public LoadProgress setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据文字的位置设置文字的Rect大小
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mLineColor);                                    // 设置绘制百分比文字属性
        mPaintText.setTextSize(mTextSize);
        mText = mCurrentValue + "%";
        mPaintText.getTextBounds(mText, 0, mText.length(), mBound);
        mTextWidth = mBound.width();

        int lineOffsetTop = (getHeight() - mLineHeight) / 2;                // 距离顶部偏移量
        float executeLineWidth;                                             // 已进行的进度的宽度
        float darkLineWidth;                                                // 灰色进度条的宽度
        float textStart;                                                    // 文字起始点
        float whiteBgStart, whiteBgStop;                                    // 文字背景区域起始点，与文字之间有个padding值

        // 判断文字显示的位置，重新设置进度条的绘制所需的偏移量
        if (isTextStatic) {
            darkLineWidth = getWidth() - mTextWidth - paddingLeft - paddingRight;
            executeLineWidth = darkLineWidth * mCurrentValue / mMaxValue;
            textStart = getWidth() - mTextWidth - paddingRight;
            whiteBgStart = darkLineWidth;
            whiteBgStop = getWidth();
        } else {
            darkLineWidth = getWidth();
            executeLineWidth = (getWidth() - mTextWidth - paddingLeft - paddingRight) * mCurrentValue / mMaxValue;
            textStart = executeLineWidth + paddingLeft;
            whiteBgStart = executeLineWidth;
            whiteBgStop = executeLineWidth + mTextWidth + paddingLeft + paddingRight;
        }


        // 绘制进度条的底色
        setPaint(mPaintLineBackground, getResources().getColor(R.color.colorGray));
        canvas.drawLine(0, lineOffsetTop, darkLineWidth, lineOffsetTop, mPaintLineBackground);

        // 绘制进度条已进行的颜色
        setPaint(mPaintLine, mLineColor);
        canvas.drawLine(0, lineOffsetTop, executeLineWidth, lineOffsetTop, mPaintLine);

        // 绘制百分比文字的背景白色区域
        setPaint(mPaintTextBackground, Color.WHITE);
        canvas.drawLine(whiteBgStart, lineOffsetTop, whiteBgStop, lineOffsetTop, mPaintTextBackground);

        // 绘制文字
        canvas.drawText(mText, textStart, lineOffsetTop + mBound.height() / 2, mPaintText);

        // 判断是否加载完成
        if (mCurrentValue == mMaxValue) {
            onLoadListener.onFinished();
        }
    }

    /**
     * 设置画笔样式
     * @param paint
     * @param color
     * @param lineHeight
     * @return
     */
    private void setPaint(Paint paint, int color) {
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(mLineHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = measureHeight(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 计算需要的宽度
     * @param defaultWidth
     * @param measureSpec
     * @return
     */
    private int measureWidth(int defaultWidth, int measureSpec) {
        int width = defaultWidth;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            width = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            width = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            width = Math.max(defaultWidth, specSize);
        }
        return width;
    }

    /**
     * 计算需要的高度
     * @param defaultHeight
     * @param measureSpec
     * @return
     */
    private int measureHeight(int defaultHeight, int measureSpec) {
        int height = defaultHeight;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            height = mTotalHeight;
        } else if (specMode == MeasureSpec.EXACTLY) {
            height = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            height = Math.max(defaultHeight, specSize);
        }
        return height;
    }

    /**
     * 获取文字的宽度
     */
    private int getTextWidth() {
        mPaintText.setTextSize(mTextSize);
        mPaintText.getTextBounds(mText, 0, mText.length(), mBound);
        return mBound.width() + 4;
    }

    /**
     * 转化为dip值
     * @param value
     * @return
     */
    private int dip(int value) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    public interface OnLoadListener {
        void onFinished();
    }
}