package com.dzenm.wanandroid.tab;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

public abstract class BaseActivity extends FragmentActivity {

    protected ViewPager2 viewPager2;
    private int style = 1;

    protected abstract int layoutId();


//    viewPager2.setPageTransformer(pageTransformer)
//    viewPager.adapter = PagerAdapter(titles, colors)
//
//    TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
//            tab.text = titles[position]
//    }.attach()


    // 设置切换的样式
    protected ViewPager2.PageTransformer pageTransformer = new ViewPager2.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            float absPos = Math.abs(position);
            if (style == 1) {
                page.setTranslationY(absPos * 500f);
                page.setTranslationX(absPos * 350f);
            } else if (style == 2) {
                float scale = absPos > 1 ? 0f : 1 - absPos;
                page.setScaleX(scale);
                page.setScaleY(scale);
            } else if (style == 3) {
                page.setRotation(position * 360);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());

        viewPager2.requestTransform();
    }

}
