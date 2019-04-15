package com.din.banner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerHelper extends PagerAdapter implements ViewPager.OnPageChangeListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static Activity activity;
    private static ViewPager viewPager;
    private static LinearLayout dotLayout;
    private static ImageView dot;

    private final static int LEFT_PAGE = 0;         // 左边显示页（根据index来调整左边页应该显示的图片）
    private final static int CENTER_PAGE = 1;       // 中间显示页（永远停留在本页）
    private final static int RIGHT_PAGE = 2;        // 右边显示页（根据index来调整右边页应该显示的图片）
    private final static int COUNT_PAGE = 0x03;     // 创建页面对象的个数

    private final static int TYPE_BITMAP = 0x01;    // Bitmap图片列表
    private final static int TYPE_RESOURCE = 0x02;  // 数组图片资源ID
    private final static int TYPE_URL = 0x03;       // 图片网址

    private List<Bitmap> bitmaps;                   // 图片资源
    private int[] images;                           // 资源ID图片
    private String[] urls;                          // 图片URL
    private int imageType;                          // 图片类型

    private List<ImageView> views;                  // 显示图片的imageView
    private List<ImageView> dotViews;               // 显示小圆点的imageView

    private int currentPage;                        // 记录当前显示的图片真正的位置
    private int size = 0;                           // 图片的个数

    private int dotSize = 25;                       // 小圆点大小
    private int dotMargin = 10;                     // 小圆点之间等间隔距离

    private Timer timer;                            // 轮播图定时器
    private long period = 2000;                     // 多长时间之后计时开始
    private long delay = 2000;                      // 多长时间后开始启动计数

    private ItemClickListener itemClickListener;    // 点击事件
    private boolean isLoop = false;                 // 是否可以无限循环
    private boolean isDot = true;                   // 设置小圆点
    private boolean isSlideStyle = true;            // 设置滑动样式
    private float distance;                         // 小圆点移动的距离

    private BannerHelper() {
    }

    private static class INSTANCE {
        public static BannerHelper bannerHelper = new BannerHelper();
    }

    public static BannerHelper getInstance() {
        return INSTANCE.bannerHelper;
    }

    /**
     * 静态方法中调用本类的静态变量
     *
     * @param activity
     * @return
     */
    public BannerHelper with(Activity activity) {
        BannerHelper.activity = activity;
        return this;
    }

    /**
     * 设置滑动的ViewPager和包裹小圆点的布局
     *
     * @param viewPager
     * @param dotLayout
     * @return
     */
    public BannerHelper init(ViewPager viewPager, LinearLayout dotLayout, ImageView dot) {
        BannerHelper.viewPager = viewPager;
        BannerHelper.dotLayout = dotLayout;
        BannerHelper.dot = dot;
        return this;
    }

    /**
     * 设置滑动的ViewPager,没有小圆点
     *
     * @param viewPager
     * @return
     */
    public BannerHelper init(ViewPager viewPager) {
        BannerHelper.viewPager = viewPager;
        isDot = false;
        return this;
    }

    /**
     * 设置图片资源
     *
     * @param bitmaps
     * @return
     */
    public BannerHelper setImage(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        size = bitmaps.size();
        imageType = TYPE_BITMAP;
        initView();
        return this;
    }

    /**
     * 设置图片资源
     *
     * @param images
     * @return
     */
    public BannerHelper setImage(int[] images) {
        this.images = images;
        size = images.length;
        imageType = TYPE_RESOURCE;
        initView();
        return this;
    }

    /**
     * 设置图片资源
     *
     * @param urls
     * @return
     */
    public BannerHelper setImage(String[] urls) {
        this.urls = urls;
        size = urls.length;
        imageType = TYPE_URL;
        initView();
        return this;
    }

    /**
     * 开始启动轮播
     */
    public BannerHelper start() {
        isLoop = true;
        timer = new Timer();
        timer.schedule(timerTask, delay, period);
        return this;
    }

    /**
     * 防止内存泄露
     *
     * @return
     */
    public BannerHelper destory() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.purge();
            timer = null;
        }
        return this;
    }

    /**
     * 单个item点击事件
     *
     * @param itemClickListener
     */
    public BannerHelper setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    /**
     * 跳转到下一页
     */
    public void nextPage() {
        viewPager.setCurrentItem(RIGHT_PAGE, true);      // 因为位置始终为1 那么下一页就始终为2
    }

    /**
     * 轮播图定时器任务
     */
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nextPage();                                    // 定时跳转下一页
                }
            });
        }
    };

    /**
     * 初始化图形控件
     */
    private void initView() {
        views = new ArrayList<>();
        if (isLoop) {                                           // 开启轮播图循环
            for (int i = 0; i < COUNT_PAGE; i++) {              // 添加图片, 第一张和最后一张切换时要无缝对接
                views.add(getImageView());
                int index = i == 0 ? size - 1 : i - 1;
                setImageResource(i, index);
            }
            timer = new Timer();
        } else {                                                // 未开启伦比图循环
            for (int i = 0; i < size; i++) {                    // 按位置添加图片
                views.add(getImageView());
                setImageResource(i, i);
            }
        }
        if (isDot) {                                            // 添加标识小圆点
            dotViews = new ArrayList<>();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dotLayout.getLayoutParams();
            params.setMargins(0, 0, dotMargin, 0);    // 设置小圆点左右之间的距离
            for (int i = 0; i < size; i++) {
                dotViews.add(getDotImageView(params));
                setDotImageResource(i, R.drawable.unselect);           // 设置未选中小圆点样式
            }
        }
        initConfig();
    }

    /**
     * 初始化设置
     */
    private void initConfig() {
        viewPager.setAdapter(this);                                    // 设置ViewPager适配器
        viewPager.setCurrentItem(CENTER_PAGE);
        viewPager.addOnPageChangeListener(this);                       // 监听ViewPager滑动
        dot.getViewTreeObserver().addOnGlobalLayoutListener(this);     // 监听小圆点滑动的跳转
    }

    /**
     * 设置图片属性
     *
     * @return
     */
    private ImageView getImageView() {
        ImageView imageView = new ImageView(activity);
        imageView.setOnClickListener(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    /**
     * 设置小圆点属性
     *
     * @return
     */
    private ImageView getDotImageView(RelativeLayout.LayoutParams params) {
        ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(params);
        dotLayout.addView(imageView, params);      // 添加到布局里面显示
        return imageView;
    }

    /**
     * 每次滑动改变currentPage
     */
    private void loopUpdateCurrentPage(int position) {
        currentPage = position > CENTER_PAGE ? currentPage + CENTER_PAGE : currentPage - CENTER_PAGE;
        if (currentPage == -CENTER_PAGE) {              // 起始页左滑，将左边那页设置为最后一页
            currentPage = size - CENTER_PAGE;           // 最后一页
        } else if (currentPage == size) {       // 最后一页右滑，将右边那页设置为起始页
            currentPage = LEFT_PAGE;                 // 第一页
        }
    }

    /**
     * 每次滑动改变currentPage
     */
    private void updateCurrentPage(int position) {
        currentPage = position > CENTER_PAGE ? currentPage + CENTER_PAGE : currentPage - CENTER_PAGE;
        if (currentPage == -CENTER_PAGE) {              // 起始页左滑，将左边那页设置为最后一页
            currentPage = size - CENTER_PAGE;           // 最后一页
        } else if (currentPage == size) {       // 最后一页右滑，将右边那页设置为起始页
            currentPage = LEFT_PAGE;                 // 第一页
        }
    }

    /**
     * 每次滑动之后对图片重新调整
     */
    private void updateImage() {
        if (currentPage == LEFT_PAGE) {                                     // 设置左页的数据
            setImageResource(LEFT_PAGE, size - CENTER_PAGE);
        } else {
            setImageResource(LEFT_PAGE, currentPage - CENTER_PAGE);
        }
        setImageResource(CENTER_PAGE, currentPage);                         // 设置中间页的数据
        if (currentPage == size - CENTER_PAGE) {                                      // 设置右页的数据
            setImageResource(RIGHT_PAGE, LEFT_PAGE);
        } else {
            setImageResource(RIGHT_PAGE, currentPage + CENTER_PAGE);
        }
        viewPager.setCurrentItem(CENTER_PAGE, false);             // 滑动结束后将当前页设置为第二页
    }

    /**
     * 每次滑动之后对小圆点重新调整
     *
     * @param position
     * @param offset
     */
    private void updateDot(int position, float offset) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dot.getLayoutParams();
        if (position == 0) {                                                // 左滑
            if (currentPage != size - 1) {
                params.leftMargin = (int) (distance * (currentPage - (1 - offset)));     // 滑动时小白点偏移量
            }
        } else if (position == 1) {                                         // 右滑
            if (currentPage != size - 1) {
                params.leftMargin = (int) (distance * (currentPage + offset));       // 滑动时小白点偏移量
            } else {
                params.leftMargin = (int) (distance * currentPage);
            }
        }
        dot.setLayoutParams(params);
    }

    /**
     * 设置当前显示的图片
     *
     * @param currentView
     * @param position
     */
    private void setImageResource(int currentView, int position) {
        switch (imageType) {
            case TYPE_RESOURCE:
                Glide.with(activity).load(images[position]).into(views.get(currentView));
                break;
            case TYPE_BITMAP:
                Glide.with(activity).load(bitmaps.get(position)).into(views.get(currentView));
                break;
            case TYPE_URL:
                Glide.with(activity).load(urls[position]).into(views.get(currentView));
                break;
            default:
                break;
        }
    }

    /**
     * 设置当前显示的图片
     *
     * @param currentView
     * @param imageSource
     */
    private void setDotImageResource(int currentView, int imageSource) {
        dotViews.get(currentView).setImageResource(imageSource);
    }

    /**
     * 小圆点滑动监听事件
     */
    @Override
    public void onGlobalLayout() {
        distance = dotLayout.getChildAt(1).getLeft() - dotLayout.getChildAt(0).getLeft();    // 两个圆点之间的距离
        dot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        if (isLoop) {
            if (isSlideStyle) {
                if (isDot) {
                    updateDot(i, v);
                }
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dot.getLayoutParams();
                params.leftMargin = (int) (distance * (currentPage));       // 滑动时小白点偏移量
                dot.setLayoutParams(params);
            }
            if (v != 0) return;                     // v等于0时处于静止，在滑动结束的时候对页面数据重新调整
            if (i == CENTER_PAGE) return;
            loopUpdateCurrentPage(i);
            updateImage();
        } else {

        }
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    /**
     * 图片的数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    /**
     * 判断是否是当前View视图
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 加载一个View视图
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = views.get(position);
        ViewGroup viewGroup = (ViewGroup) imageView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    /**
     * 移除一个View视图
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(currentPage);
        }
    }

    /**
     * 点击事件接口
     */
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}