package com.din.wanandroid.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews;

    public ViewPagerAdapter(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    /**
     * 获取当前要显示对象的数量
     * @return
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * 判断是否复用对象生成界面
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 从ViewGroup中移除当前对象（图片）
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        int newPosition = position % imageViews.size();
        container.removeView(imageViews.get(newPosition));
    }

    /**
     * 当前要显示的对象（图片）
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int newPosition = position % imageViews.size();
        ImageView imageView = imageViews.get(newPosition);
        // a. 把View对象添加到container中
        container.addView(imageView);
        // b. 把View对象返回给框架, 适配器
        return imageView; // 必须重写, 否则报异常
    }
}