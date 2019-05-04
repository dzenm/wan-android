package com.din.banner.unlimited;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import com.din.banner.R;

public class ScrollerPage implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private LinearLayout mPointLayout;

    private Activity mActivity;

    private int[] mImages;
    private ImageView[] mPointViews;
    private ImageView[] mImageViews;

    /*
     *
     */
    private float mDistance;

    /*
     * 指示器之间的间距
     */
    private int mPointMargin = 10;

    /*
     * 图片之间的间距
     */
    private int mImageMargin = 15;

    /*
     * 指示器的显示的大小
     */
    private int mPointSize = 40;

    /*
     * 页面的个数
     */
    private int mSize = 0;

    /*
     * 当前页面的位置
     */
    private int mCurrentPosition = 500;

    public static ScrollerPage newInstance() {
        ScrollerPage scrollerPage = new ScrollerPage();
        return scrollerPage;
    }

    /**
     * @param activity
     * @param viewPager
     * @param layout
     * @return
     */
    public ScrollerPage init(Activity activity, ViewPager viewPager, LinearLayout layout) {
        mActivity = activity;
        mViewPager = viewPager;
        mPointLayout = layout;
        return this;
    }

    /**
     * 设置图像的资源
     * @param images
     * @return
     */
    public ScrollerPage setImages(@NonNull int[] images) {
        mImages = images;
        mSize = mImages.length;
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        initView();
        initViewPager();         // 设置图片的显示
    }

    /**
     * 跳转到下一页
     */
    public void nextPage() {
        mCurrentPosition++;
    }

    /**
     * 初始化View
     */
    private void initView() {
        mImageViews = new ImageView[mSize];
        mPointViews = new ImageView[mSize];              // 小圆点的个数

        RelativeLayout.LayoutParams paramsRL = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        LinearLayout.LayoutParams paramsLL = getPointLayoutParams();

        for (int i = 0; i < mSize; i++) {
            ImageView imageView = getImageView(paramsRL, mImages[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);     // 设置图片的适配
            imageView.setPadding(mImageMargin, mImageMargin, mImageMargin, mImageMargin);
            mImageViews[i] = imageView;

            boolean isFirst = i == 0;
            ImageView pointView = getImageView(paramsLL, getPointImageResource(isFirst));
            pointView.setSelected(isFirst ? true : false);
            mPointViews[i] = pointView;
            mPointLayout.addView(pointView);
        }

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
        ImageView imageView = new ImageView(mActivity);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(resId);
        return imageView;
    }

    /**
     * 获取小圆点的图像资源
     * @param isSelect
     * @return
     */
    private int getPointImageResource(boolean isSelect) {
        return isSelect ? R.drawable.select : R.drawable.unselect;
    }

    /**
     * 初始化ViewPager设置
     */
    private void initViewPager() {
        UnlimitedViewPagerAdapter adapter = new UnlimitedViewPagerAdapter(mImageViews);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        /**
         * 循环播放的原理：ViewPager滑动的页面设置无限大，然后根据需要循环播放的页面的个数
         * 用position除以个数取余。实现页面显示的循环，但position是从0开始无限增长
         */
        for (int i = 0; i < mSize; i++) {       // 滑动之后，显示当前停留的point的位置
            mPointViews[i].setSelected(position % mSize == i ? true : false);    // 设置是否选中
            mPointViews[i].setImageResource(getPointImageResource(position % mSize == i)); // 设置当前显示的图标，区别没选中的
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}