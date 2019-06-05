package com.din.banner.unlimited;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import com.dzenm.banner.R;

public class ScrollerPage extends RelativeLayout implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    private ViewPager mViewPager;

    /*
     * 未选中圆点的ViewGroup(和图片个数相同)
     */
    private LinearLayout mPointUnselected;

    /*
     * 选中的圆点的ViewGroup(只有一个选中的圆点)
     */
    private RelativeLayout mPointSelectedLayout;

    /*
     * 选中的圆点View
     */
    private ImageView mPointSelected;

    /*
     * 两个圆点左边起始之间的距离
     */
    private float mPointDistance;

    /*
     * 圆点之间的间距
     */
    private int mPointMargin = 10;

    /*
     * 圆点大小
     */
    private int mPointSize = 40;

    /*
     * 显示图片的View
     */
    private ImageView[] mImageViews;

    /*
     * 图片资源
     */
    private int[] mImages;

    /*
     * 图片之间的间距
     */
    private int[] mImageMargins;

    /*
     * 当前页面的位置
     */
    private int mCurrentPosition;

    /*
     * 是否可以循环
     */
    private boolean isRepeat = true;

    /*
     * 默认图片的边距
     */
    private int mDefaultImageMargin = 10;

    public ScrollerPage(Context context) {
        this(context, null);
    }

    public ScrollerPage(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollerPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView();
    }

    /**
     * 设置图像的资源
     * @param images
     * @return
     */
    public ScrollerPage setImages(@NonNull int[] images) {
        mImages = images;
        return this;
    }

    /**
     * 设置图片的边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public ScrollerPage setImageMargins(int left, int top, int right, int bottom) {
        mImageMargins[0] = left;
        mImageMargins[1] = top;
        mImageMargins[2] = right;
        mImageMargins[3] = bottom;
        return this;
    }

    /**
     * 跳转到下一页
     */
    public void nextPage() {
        mViewPager.setCurrentItem(mCurrentPosition++);
    }

    /**
     * 绘制
     */
    public void show() {
        int length = mImages.length;
        mCurrentPosition = length << 10 + 1;
        i️nitialImageView(length);
        initialPointView(length);
        initialViewPager();
    }

    /**
     * 初始化View
     */
    private void initialView() {
        View view = inflate(getContext(), R.layout.layout_banner_unlimited, this);
        mPointUnselected = view.findViewById(R.id.ll_point_unselected);
        mPointSelectedLayout = view.findViewById(R.id.rl_point_selected);
        mViewPager = view.findViewById(R.id.viewPager);

        mImageMargins = new int[4];
        setImageMargins(mDefaultImageMargin, mDefaultImageMargin, mDefaultImageMargin, mDefaultImageMargin);
    }

    /**
     * 初始化图片View
     * @param length
     */
    private void i️nitialImageView(int length) {
        // 设置image的边距
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        mImageViews = new ImageView[length];
        for (int i = 0; i < length; i++) {       // 动态加载图片
            ImageView imageView = getImageView(layoutParams, mImages[i]);
            // 设置图片的适配
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(mImageMargins[0], mImageMargins[1], mImageMargins[2], mImageMargins[3]);
            mImageViews[i] = imageView;
        }
    }

    /**
     * 初始化圆点View
     * @param length
     */
    private void initialPointView(int length) {
        // 设置point的边距
        LinearLayout.LayoutParams layoutParams = getPointLayoutParams();
        for (int i = 0; i < length; i++) {
            // 动态加载未选中的圆点
            ImageView imageView = getImageView(layoutParams, R.drawable.unselect);
            mPointUnselected.addView(imageView);
        }
        // 添加选中的圆点
        mPointSelected = getImageView(layoutParams, R.drawable.select);
        mPointSelectedLayout.addView(mPointSelected);
    }

    /**
     * 获取小圆点的LayoutParams
     * @return
     */
    private LinearLayout.LayoutParams getPointLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mPointSize, mPointSize);
        layoutParams.setMargins(mPointMargin, 0, mPointMargin, 0);    // 设置小圆点左右之间的距离
        return layoutParams;
    }

    /**
     * 创建一个动态ImageView
     * @param layoutParams
     * @param resId
     * @return
     */
    private ImageView getImageView(ViewGroup.LayoutParams layoutParams, int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(resId);
        return imageView;
    }

    /**
     * 初始化ViewPager设置
     */
    private void initialViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(mImageViews, isRepeat);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mPointSelected.getViewTreeObserver().addOnGlobalLayoutListener(this);                   // 任何一个组件都可以得到视图树
    }

    /**
     * 获取根View的LayoutParams
     * @return
     */
    public RelativeLayout.LayoutParams getDecorView() {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mViewPager.getLayoutParams();
        return getDecorView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) return;
        // 导航页滑动的时候调用, positionOffset: 滑动的百分比（[0,1}）
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPointSelected.getLayoutParams();
//        layoutParams.leftMargin = (int) (mPointDistance * (positionOffset + position % mImages.length)) + mPointMargin;
        int length = mImages.length;
        float offsetDistance = onPointBehavior(position, positionOffset, positionOffsetPixels, length);
        layoutParams.leftMargin = (int) (mPointDistance * offsetDistance + position % length) + mPointMargin;
        mPointSelected.setLayoutParams(layoutParams);
    }

    // 记录上一次滑动的positionOffsetPixels值
    private int lastOffset = -1;
    private boolean isLeft = false;

    protected float onPointBehavior(int position, float positionOffset, int positionOffsetPixels, int length) {
        float offsetDistance = 0;
        scrollerDirection(positionOffsetPixels);
        if (position % length == 0 && isLeft) {
            if (positionOffset > 0.5) {
                offsetDistance = positionOffset - 1;
            } else {
                offsetDistance = (length - 1) + positionOffset;
            }
        } else if (position % length == length - 1 && !isLeft) {
            if (positionOffset < 0.5) {
                offsetDistance = positionOffset - 1;
            }
        } else {
            offsetDistance = positionOffset;
        }

        return offsetDistance;
    }

    /**
     * 判断滑动的方向
     * @param positionOffsetPixels
     */
    private void scrollerDirection(int positionOffsetPixels) {
        if (lastOffset != -1) return;
        if (positionOffsetPixels > lastOffset) {
            isLeft = false;
        } else if (positionOffsetPixels <= lastOffset) {
            isLeft = true;
        }
        lastOffset = positionOffsetPixels;
    }

    @Override
    public void onPageSelected(int position) {
        lastOffset = -1;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onGlobalLayout() {
        mPointDistance = mPointUnselected.getChildAt(1).getLeft() - mPointUnselected.getChildAt(0).getLeft();
        // 移除视图树的监听
        mPointSelected.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}