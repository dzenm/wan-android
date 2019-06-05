package com.din.banner.limited;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.dzenm.banner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dinzhenyan
 * @date 2019-04-21 18:06
 * @IDE Android Studio
 * <p>
 * 轮播图
 */
public class ScrollerPage implements ViewPager.OnPageChangeListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private final static int LEFT_PAGE = 0;         // 左边显示页（根据index来调整左边页应该显示的图片）
    private final static int CENTER_PAGE = 1;       // 中间显示页（永远停留在本页）
    private final static int RIGHT_PAGE = 2;        // 右边显示页（根据index来调整右边页应该显示的图片）
    private final static int COUNT_PAGE = 3;        // 创建页面对象的个数

    private static Activity activity;
    private ViewPager viewPager;
    private LinearLayout mPointLayout;
    private ImageView mIvPoint;

    private String[] mLinks;                        // 图片URL
    private List<ImageView> mViews;                 // 显示图片的imageView
    private List<ImageView> mPointViews;            // 显示小圆点的imageView

    private int mSize;                              // 图片的个数
    private int mCurrentPosition;                   // 记录当前显示的图片真正的位置
    private int mPointSize = 25;                    // 小圆点大小
    private int mPointMargin = 10;                  // 小圆点之间等间隔距离
    private int mImageMargin = 15;                  // ImageView之间的间距值

    private long period = 2000;                     // 多长时间之后计时开始
    private long delay = 4000;                      // 多长时间后开始启动计数

    private boolean isLoop = true;                  // 是否可以无限循环
    private boolean isShowMovePoint = true;         // 设置小圆点
    private float mDistance;                        // 小圆点移动的距离

    private OnItemClickListener onItemClickListener;// 点击事件
    private Timer mTimer;                           // 轮播图定时器

    /**
     * 静态方法中调用本类的静态变量
     * @param activity
     * @return
     */
    public ScrollerPage with(Activity activity) {
        ScrollerPage.activity = activity;
        return this;
    }

    /**
     * @param viewPager
     * @param pointLayout
     * @param ivPoint
     * @return
     */
    public ScrollerPage init(ViewPager viewPager, LinearLayout pointLayout, ImageView ivPoint) {
        this.viewPager = viewPager;
        mPointLayout = pointLayout;
        mIvPoint = ivPoint;
        return this;
    }

    /**
     * @param viewPager
     * @return
     */
    public ScrollerPage init(ViewPager viewPager) {
        this.viewPager = viewPager;
        isShowMovePoint = false;
        return this;
    }

    /**
     * 设置图片
     * @param urls
     * @return
     */
    public ScrollerPage setImage(String[] urls) {
        this.mLinks = urls;
        mSize = mLinks.length;
        initView();
        return this;
    }

    /**
     * 是否开启无限循环，默认为true
     * @param loop
     * @return
     */
    public ScrollerPage setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    /**
     * 开始启动
     */
    public ScrollerPage start() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(mTimerTask, delay, period);
        return this;
    }

    /**
     * 防止内存泄露
     * @return
     */
    public ScrollerPage destory() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        return this;
    }

    /**
     * 单个item点击事件
     * @param itemClickListener
     */
    public ScrollerPage setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
        return this;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 跳转到下一页
     */
    public void nextPage() {
        viewPager.setCurrentItem(RIGHT_PAGE, true);     // 因为位置始终为1 那么下一页就始终为2
    }

    /**
     * 跳转到上一页
     */
    public void lastPage() {
        viewPager.setCurrentItem(LEFT_PAGE, true);      // 因为位置始终为1 那么上一页就始终为0
    }

    /**
     * 定时跳转
     */
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nextPage();
                }
            });
        }
    };

    /**
     * 初始化图形控件
     */
    private void initView() {
        mViews = new ArrayList<>();
        ViewPagerAdapter adapter = new ViewPagerAdapter();

        for (int i = 0; i < COUNT_PAGE; i++) {
            mViews.add(getImageView(true));
            int index = isLoop ? (i == 0 ? mSize - 1 : i - 1) : i;
            setImage(i, index);
        }
        if (isShowMovePoint) {  // 添加标识小圆点
            mPointViews = new ArrayList<>();
            RelativeLayout.LayoutParams paramsPoint = (RelativeLayout.LayoutParams) mPointLayout.getLayoutParams();
            paramsPoint.setMargins(0, 0, mPointMargin, 0);      // 设置小圆点左右之间的距离
            for (int i = 0; i < mSize; i++) {
                ImageView imageView = getImageView(false);
                imageView.setLayoutParams(paramsPoint);
                mPointLayout.addView(imageView, paramsPoint);
                mPointViews.add(imageView);
                setPoint(i, R.drawable.unselect);           // 设置未选中小圆点样式
            }
            mIvPoint.getViewTreeObserver().addOnGlobalLayoutListener(this);     // 监听小圆点滑动的跳转
        }

        adapter.setViews(mViews);
        viewPager.setAdapter(adapter);                                          // 设置ViewPager适配器
        if (isLoop) {
            viewPager.setCurrentItem(CENTER_PAGE);
        }
        viewPager.addOnPageChangeListener(this);                                // 监听ViewPager滑动
    }

    /**
     * 设置图片属性
     * @return
     */
    private ImageView getImageView(boolean isAddMargin) {
        ImageView imageView = new ImageView(activity);
        imageView.setOnClickListener(this);
        if (isAddMargin)
            imageView.setPadding(dp2px(mImageMargin), dp2px(mImageMargin), dp2px(mImageMargin), dp2px(mImageMargin));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    /**
     * 设置当前显示的图片
     * @param currentView
     * @param position
     */
    private void setImage(int currentView, int position) {
        Glide.with(activity).load(mLinks[position]).into(mViews.get(currentView));
    }

    /**
     * 设置当前显示的图片
     * @param currentView
     * @param imageSource
     */
    private void setPoint(int currentView, int imageSource) {
        mPointViews.get(currentView).setImageResource(imageSource);
    }

    /**
     * 小圆点滑动监听事件
     */
    @Override
    public void onGlobalLayout() {
        mDistance = mPointLayout.getChildAt(1).getLeft() - mPointLayout.getChildAt(0).getLeft();    // 两个圆点之间的距离
        mIvPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private int lastPosition = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) {  // positionOffset等于0时处于静止，在滑动结束的时候对页面数据重新调整
            if (isLoop) {
                if (position == CENTER_PAGE) return;
                setLoopViewPosition(position);
                setLoopImagePosition();
            } else {
                setUnLoopViewPosition(position);
                setUnLoopImagePosition();
            }
        } else {
            if (!isLoop) return;
            lastPosition = position;
            if (isShowMovePoint) {  // 提示的小圆点
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvPoint.getLayoutParams();
                float offsetDistance = pointBehavior(position, positionOffset, mCurrentPosition, mSize);
                params.leftMargin = (int) (offsetDistance * mDistance);
                mIvPoint.setLayoutParams(params);
            }
        }
    }

    /**
     * 每次滑动之后对小圆点重新调整
     * @param position
     * @param offset
     */
    protected float pointBehavior(int position, float offset, int mCurrentPosition, int size) {
        float offsetDistance = 0;
        if (position == 0) {                    // 左滑(offset从1.0-0.0结束)
            if (mCurrentPosition == 0) {         // 是否是第一个向左滑动，并且显示到最后一个
                if (offset > 0.5) {
                    offsetDistance = offset - 1;
                } else {
                    offsetDistance = (size - 1) + offset;
                }
            } else {
                offsetDistance = mCurrentPosition - (1 - offset);
            }
        } else if (position == 1) {             // 右滑(offset从0.0-1.0结束)
            if (mCurrentPosition == size - 1) {  // 是否是最后一个向左滑动，并且显示到第一个
                if (offset < 0.5) {
                    offsetDistance = (size - 1) + offset;
                } else {
                    offsetDistance = offset - 1;
                }
            } else {
                offsetDistance = mCurrentPosition + offset;
            }
        }
        return offsetDistance;
    }

    /**
     * 每次滑动改变currentPage
     */
    private void setLoopViewPosition(int position) {
        mCurrentPosition = position > CENTER_PAGE ? mCurrentPosition + 1 : mCurrentPosition - 1;
        if (mCurrentPosition == -1) {              // 起始页左滑，将左边那页设置为最后一页
            mCurrentPosition = mSize - 1;          // 最后一页
        } else if (mCurrentPosition == mSize) {    // 最后一页右滑，将右边那页设置为起始页
            mCurrentPosition = 0;                  // 第一页
        }
    }

    /**
     * 每次滑动之后对图片重新调整
     */
    private void setLoopImagePosition() {
        /*
         * 设置左页的数据
         * 判断当前位置是否为数据起始位置，如果是（即0）将左页的数据设置为最后一个数据
         */
        if (mCurrentPosition == 0) {
            setImage(LEFT_PAGE, mSize - 1);
        } else {
            setImage(LEFT_PAGE, mCurrentPosition - 1);
        }

        /*
         * 设置中间页的数据
         */
        setImage(CENTER_PAGE, mCurrentPosition);

        /*
         * 设置右页的数据
         * 判断当前位置是否为数据末尾，如果是（即size-1）将右边的数据设置为第一个数据
         */
        if (mCurrentPosition == mSize - 1) {
            setImage(RIGHT_PAGE, 0);
        } else {
            setImage(RIGHT_PAGE, mCurrentPosition + 1);
        }

        /*
         * 滑动结束后将当前页设置为第二页
         * 即ViewPager的当前显示position，本来为1，每次滑动之后会变为2，需要手动将它设置为1
         */
        viewPager.setCurrentItem(CENTER_PAGE, false);
    }

    /**
     * 每次滑动改变currentPage
     */
    private void setUnLoopViewPosition(int position) {
        if (lastPosition != -1) {
            mCurrentPosition = position > CENTER_PAGE ? mCurrentPosition + 1 : mCurrentPosition - 1;
            if (mCurrentPosition == -1) {              // 起始页左滑，将左边那页设置为最后一页
                mCurrentPosition = mSize - 1;          // 最后一页
            } else if (mCurrentPosition == mSize) {    // 最后一页右滑，将右边那页设置为起始页
                mCurrentPosition = 0;                  // 第一页
            }
        } else {
            lastPosition = 0;
        }
    }


    /**
     * 每次滑动之后对图片重新调整
     */
    private void setUnLoopImagePosition() {
        /*
         * 设置左页的数据
         * 判断当前位置是否为数据起始位置，如果是（即0）将左页的数据设置为最后一个数据
         */
        if (mCurrentPosition == 0) {
            setImage(LEFT_PAGE, mCurrentPosition);
            setImage(CENTER_PAGE, mCurrentPosition + 1);
            setImage(RIGHT_PAGE, mCurrentPosition + 2);
        } else if (mCurrentPosition == mSize - 1) {
            setImage(LEFT_PAGE, mCurrentPosition - 2);
            setImage(CENTER_PAGE, mCurrentPosition - 1);
            setImage(RIGHT_PAGE, mCurrentPosition);
        } else {
            setImage(LEFT_PAGE, mCurrentPosition - 1);
            setImage(CENTER_PAGE, mCurrentPosition);
            setImage(RIGHT_PAGE, mCurrentPosition + 1);
        }

        /*
         * 滑动结束后将当前页设置为第二页
         * 即ViewPager的当前显示position，本来为1，每次滑动之后会变为2，需要手动将它设置为1
         */
        if (mCurrentPosition == 0) {
            viewPager.setCurrentItem(LEFT_PAGE, false);
        } else if ((mCurrentPosition < mSize - 1) && (mCurrentPosition > 0)) {
            viewPager.setCurrentItem(CENTER_PAGE, false);
        } else if (mCurrentPosition == mSize - 1) {
            viewPager.setCurrentItem(RIGHT_PAGE, false);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    /**
     * 当用手指滑动时，在手指滑动的时刻触发state==1
     * 滑动停止时，先调用state==2，在调用state==0
     * <p>
     * 当不用手指滑动时，滑动的时刻不会调用state==1
     * 直接等滑动结束时，先调用state==2，在调用state==0
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(mCurrentPosition);
        }
    }

    /**
     * 点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
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