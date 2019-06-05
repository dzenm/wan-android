package com.din.banner.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.dzenm.banner.R;

/**
 * @author dinzhenyan
 * @date 2019-05-04 03:06
 * @IDE Android Studio
 */
public class ImageView extends AppCompatImageView {

    /*
     * 是否显示为圆形，如果为圆形则设置的corner无效
     */
    private boolean isCircle;

    /*
     * border、inner_border是否覆盖图片
     */
    private boolean isCoverImage;

    /*
     * 遮罩颜色
     */
    private int mMaskColor;

    /*
     * 可显示区域的宽度
     */
    private int mWidth;

    /*
     * 可显示区域的高度
     */
    private int mHeight;

    /*
     * 圆形区域的半径
     */
    private float mRadius;

    /*
     * 边框宽度
     */
    private int mBorderWidth;

    /*
     * 边框颜色
     */
    private int mBorderColor;

    /*
     * 内层边框宽度
     */
    private int mInnerBorderWidth;

    /*
     * 内层边框充色
     */
    private int mInnerBorderColor;

    /*
     * 圆角半径(统一设置)，优先级高于单独设置每个角的半径
     */
    private int mCornerRadius;

    /*
     * 圆角半径数组，上左，上右，下右，下左依次排列
     */
    private int[] mCornerRadiuses;

    /*
     * 所有边框的半径值
     */
    private float[] mBorderRadii;

    /*
     * 图片的半径值
     */
    private float[] mImageRadii;

    /*
     * 图片占的矩形区域
     */
    private RectF mImageRectF;

    /*
     * 边框的矩形区域
     */
    private RectF mBorderRectF;

    /*
     * 用来裁剪图片的ptah
     */
    private Path mPath;

    /*
     * 图片区域大小的Path
     */
    private Path mImagePath;

    /*
     * 绘制边框的画笔
     */
    private Paint mPaint;

    /*
     * 方向的个数
     */
    private int mDirectionSize = 4;

    private int defBorderColor = Color.WHITE;

    private int defInnerBorderColor = Color.WHITE;

    private int defMaskColor = Color.TRANSPARENT;

    private Xfermode mXfermode;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderRadii = new float[2 * mDirectionSize];
        mImageRadii = new float[2 * mDirectionSize];
        mCornerRadiuses = new int[mDirectionSize];

        mBorderRectF = new RectF();
        mImageRectF = new RectF();

        mPaint = new Paint();
        mPath = new Path();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        } else {
            mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
            mImagePath = new Path();
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageView, 0, 0);

        isCoverImage = ta.getBoolean(R.styleable.ImageView_isCoverImage, isCoverImage);
        isCircle = ta.getBoolean(R.styleable.ImageView_isCircle, isCircle);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.ImageView_borderWidth, mBorderWidth);
        mBorderColor = ta.getColor(R.styleable.ImageView_borderColor, defBorderColor);
        mInnerBorderWidth = ta.getDimensionPixelSize(R.styleable.ImageView_innerBorderWidth, mInnerBorderWidth);
        mInnerBorderColor = ta.getColor(R.styleable.ImageView_innerBorderColor, defInnerBorderColor);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.ImageView_cornerRadius, mCornerRadius);
        mCornerRadiuses[0] = ta.getDimensionPixelSize(R.styleable.ImageView_topLeftCornerRadius, mCornerRadiuses[0]);
        mCornerRadiuses[1] = ta.getDimensionPixelSize(R.styleable.ImageView_topRightCornerRadius, mCornerRadiuses[1]);
        mCornerRadiuses[2] = ta.getDimensionPixelSize(R.styleable.ImageView_bottomRightCornerRadius, mCornerRadiuses[2]);
        mCornerRadiuses[3] = ta.getDimensionPixelSize(R.styleable.ImageView_bottomLeftCornerRadius, mCornerRadiuses[3]);
        mMaskColor = ta.getColor(R.styleable.ImageView_maskColor, defMaskColor);

        ta.recycle();

        calculateBorderAndImageRadii();
        clearInnerBorderWidth();
    }

    /**
     * 计算外边框的RectF
     */
    private void initBorderRectF() {
        if (!isCircle)
            mBorderRectF.set(mBorderWidth / 2.0f, mBorderWidth / 2.0f, mWidth - mBorderWidth / 2.0f, mHeight - mBorderWidth / 2.0f);
    }

    /**
     * 计算图片原始区域的RectF
     */
    private void initImageRectF() {
        if (isCircle) {
            mRadius = Math.min(mWidth, mHeight) / 2.0f;
            mImageRectF.set(mWidth / 2.0f - mRadius, mHeight / 2.0f - mRadius, mWidth / 2.0f + mRadius, mHeight / 2.0f + mRadius);
        } else {
            mImageRectF.set(0, 0, mWidth, mHeight);
            if (isCoverImage) mImageRectF = mBorderRectF;
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private void calculateBorderAndImageRadii() {
        if (isCircle) return;
        if (mCornerRadius > 0) {
            for (int i = 0; i < mBorderRadii.length; i++) {
                mBorderRadii[i] = mCornerRadius;
                mImageRadii[i] = mCornerRadius - mBorderWidth / 2.0f;
            }
        } else {
            for (int i = 0; i < mCornerRadiuses.length; i++) {
                mBorderRadii[2 * i] = mBorderRadii[2 * i + 1] = mCornerRadiuses[i];
                mImageRadii[2 * i] = mImageRadii[2 * i + 1] = mCornerRadiuses[i] - mBorderWidth / 2.0f;
            }
        }
    }

    private void calculateBorderRadiiAndRectF(boolean reset) {
        if (reset) mCornerRadius = 0;
        calculateBorderAndImageRadii();
        initBorderRectF();
        invalidate();
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private void clearInnerBorderWidth() {
        if (!isCircle) mInnerBorderWidth = 0;
    }

    public void setCoverImage(boolean isCoverImage) {
        this.isCoverImage = isCoverImage;
        initImageRectF();
        invalidate();
    }

    public void isCircle(boolean isCircle) {
        this.isCircle = isCircle;
        clearInnerBorderWidth();
        initImageRectF();
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = dp2px(borderWidth);
        calculateBorderRadiiAndRectF(false);
    }

    public void setBorderColor(@ColorInt int borderColor) {
        this.mBorderColor = borderColor;
        invalidate();
    }

    public void setInnerBorderWidth(int innerBorderWidth) {
        this.mInnerBorderWidth = dp2px(innerBorderWidth);
        clearInnerBorderWidth();
        invalidate();
    }

    public void setInnerBorderColor(@ColorInt int innerBorderColor) {
        this.mInnerBorderColor = innerBorderColor;
        invalidate();
    }

    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = dp2px(cornerRadius);
        calculateBorderRadiiAndRectF(false);
    }

    public void setCornerRadius(int topLeftCornerRadius, int topRightCornerRadius, int bottomRightCornerRadius, int bottomLeftCornerRadius) {
        mCornerRadiuses[0] = topLeftCornerRadius;
        mCornerRadiuses[1] = topRightCornerRadius;
        mCornerRadiuses[2] = bottomRightCornerRadius;
        mCornerRadiuses[3] = bottomLeftCornerRadius;
        calculateBorderRadiiAndRectF(true);
    }

    public void setMaskColor(@ColorInt int mMaskColor) {
        this.mMaskColor = mMaskColor;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        initBorderRectF();
        initImageRectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(mImageRectF, null, Canvas.ALL_SAVE_FLAG);
        if (!isCoverImage) {
            float sx = 1.0f * (mWidth - 2 * mBorderWidth - 2 * mInnerBorderWidth) / mWidth;
            float sy = 1.0f * (mHeight - 2 * mBorderWidth - 2 * mInnerBorderWidth) / mHeight;
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, mWidth / 2.0f, mHeight / 2.0f);
        }
        super.onDraw(canvas);
        mPaint.reset();
        mPath.reset();
        if (isCircle) {
            mPath.addCircle(mWidth / 2.0f, mHeight / 2.0f, mRadius, Path.Direction.CCW);
        } else {
            mPath.addRoundRect(mImageRectF, mImageRadii, Path.Direction.CCW);
        }

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(mXfermode);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(mPath, mPaint);
        } else {
            mImagePath.addRect(mImageRectF, Path.Direction.CCW);
            // 计算tempPath和mPath的差集
            mImagePath.op(mPath, Path.Op.DIFFERENCE);
            canvas.drawPath(mImagePath, mPaint);
        }
        mPaint.setXfermode(null);

        // 绘制遮罩
        if (mMaskColor != 0) {
            mPaint.setColor(mMaskColor);
            canvas.drawPath(mPath, mPaint);
        }
        // 恢复画布
        canvas.restore();
        // 绘制边框
        drawBorders(canvas);
    }

    private void drawBorders(Canvas canvas) {
        if (isCircle) {
            if (mBorderWidth > 0) drawCircleBorder(canvas, mBorderWidth, mBorderColor, mRadius - mBorderWidth / 2.0f);
            if (mInnerBorderWidth > 0)
                drawCircleBorder(canvas, mInnerBorderWidth, mInnerBorderColor, mRadius - mBorderWidth - mInnerBorderWidth / 2.0f);
        } else {
            if (mBorderWidth > 0) drawRectFBorder(canvas, mBorderWidth, mBorderColor, mBorderRectF, mBorderRadii);
        }
    }

    private void drawCircleBorder(Canvas canvas, int borderWidth, int borderColor, float radius) {
        initBorderPaint(borderWidth, borderColor);
        mPath.addCircle(mWidth / 2.0f, mHeight / 2.0f, radius, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawRectFBorder(Canvas canvas, int borderWidth, int borderColor, RectF rectF, float[] radii) {
        initBorderPaint(borderWidth, borderColor);
        mPath.addRoundRect(rectF, radii, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
    }

    private void initBorderPaint(int borderWidth, int borderColor) {
        mPath.reset();
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(borderColor);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * dp转px
     * @param value
     * @return
     */
    public static int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}
