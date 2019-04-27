package com.din.wanandroid.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerUtils implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private LinearLayout layout;
    private int[] images;
    private Activity activity;
    private ImageView[] pointViews;               // 小圆点

    private int pointMargin = 10;                 // 设置小圆点margin
    private int pointSize = 25;                 // 设置小圆点size
    private int length = 0;
    private int currentNum = 500, temp = 500;
    private static final long DELAY = 1000;
    private ViewPagerAdapter adapter;

    private Timer timer;
    private TimerTask timerTask;
    private boolean stop = false;

    public static ViewPagerUtils newInstance() {
        ViewPagerUtils viewPagerUtils = null;
        if (viewPagerUtils == null) {
            viewPagerUtils = new ViewPagerUtils();
        }
        return viewPagerUtils;
    }

    public ViewPagerUtils setInit(Activity activity, ViewPager viewPager, LinearLayout layout) {
        this.activity = activity;
        this.viewPager = viewPager;
        this.layout = layout;
        return this;
    }

    public ViewPagerUtils setImages(@NonNull int[] images) {
        this.images = images;
        length = images.length;
        pointViews = new ImageView[length];              // 小圆点的个数
        return this;
    }

    public void show() {
        setImageInflater();         // 设置图片的显示
        startTimer();
    }

    private void startTimer() {
        temp = currentNum;
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onPageSelected(currentNum);
                            viewPager.setCurrentItem(currentNum);
                            nextPage();
                        }
                    });
                }
            };
        }
        timer.schedule(timerTask, DELAY, DELAY);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (!isStop()) {
            stop = true;
            temp = currentNum;
        }
    }

    private boolean isStop() {
        return stop;
    }

    private void nextPage() {
        currentNum++;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!isStop()) {
            stopTimer();
        }
    }

    public void onPageSelected(int position) {
        // 循环播放的原理：ViewPager滑动的页面设置无限大，然后根据需要循环播放的页面的个数，用position除以个数取余。实现页面显示的循环，但position是从0开始无限增长
        for (int i = 0; i < length; i++) {       // 滑动之后，显示当前停留的point的位置
            pointViews[i].setSelected(position % length == i ? true : false);    // 设置是否选中
            pointViews[i].setImageResource(position % length == i ? com.din.banner.R.drawable.select : com.din.banner.R.drawable.unselect);       // 设置当前显示的图标，区别没选中的
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (isStop()) {
            startTimer();
        }
    }

    private void setImageInflater() {
        ViewPager.LayoutParams params = new ViewPager.LayoutParams();   // 图片的外层布局ViewPager
        List<ImageView> imageViews = new ArrayList<>();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pointSize, pointSize);
        layoutParams.setMargins(pointMargin, 0, pointMargin, 0);    // 设置小圆点左右之间的距离

        for (int i = 0; i < length; i++) {
            ImageView picture = new ImageView(activity);      // 添加图片显示
            picture.setLayoutParams(params);      // 设置布局
            picture.setImageResource(images[i]);
            picture.setScaleType(ImageView.ScaleType.FIT_START);     // 设置图片的适配
            imageViews.add(picture);
            if (i == length - 1) {
                picture.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }

            ImageView point = new ImageView(activity);      // //添加小圆点的显示
            point.setLayoutParams(layoutParams);
            point.setImageResource(i == 0 ? com.din.banner.R.drawable.select : com.din.banner.R.drawable.unselect);     // 设置自定义小圆点
            point.setSelected(i == 0 ? true : false);        // 默认启动时选择第一个
            pointViews[i] = point;        // 得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态
            layout.addView(point);      // 添加到布局里面显示
        }
        adapter = new ViewPagerAdapter(imageViews);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentNum);
    }
}