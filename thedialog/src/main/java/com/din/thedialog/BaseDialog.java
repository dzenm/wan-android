package com.din.thedialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @param <T>
 * @author dinzhenyan
 * @see AlertDialog
 * @see BaseDialog
 */
public abstract class BaseDialog<T extends BaseDialog> extends AlertDialog {

    /*
     * root view
     */
    protected View view;

    /*
     * dialog背景，可通过setBackground()方法设置background
     * 默认的background在构造方法里，为白色圆角背景
     */
    protected int background;

    /*
     * dialog边距值， 可通过setMargin()方法设置margin
     * margin的值为上下边距，左右边距是上下边距的两倍
     */
    protected int margin;

    /*
     * dialog位置，可通过setGravity设置gravity，默认显示在中间
     */
    protected int gravity;

    /*
     * dialog动画，根据gravity的位置显示动画，自定义动画通过setAnimator设置
     * dialog显示在顶部时从顶部往下弹出
     * dialog显示在底部时从底部往上弹出
     * dialog显示在中间时从中间缩放显示
     */
    protected int animator;

    /*
     * 是否是默认背景
     */
    protected boolean isDefaultBackground = true;

    /*
     * 是否是默认位置
     */
    protected boolean isDefaultGravity = true;

    /*
     * 是否是默认边距
     */
    protected boolean isDefaultMargin = true;

    /*
     * 是否是默认动画
     */
    protected boolean isDefaultAnimator = true;

    /*
     * 去除dialog灰色区域
     */
    protected boolean isTranslucent = false;

    /*
     * dialog显示在中间
     */
    private boolean isCenter = true;

    static {
        // 开启在TextView的drawableTop或者其他额外方式使用矢量图渲染
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /************************************* 以下为自定义提示内容 *********************************/

    /**
     * Dialog的margin值
     * @param margin
     * @return
     */
    public BaseDialog<T> setMargin(int margin) {
        this.margin = margin;
        isDefaultMargin = false;
        return this;
    }

    /**
     * Dialog显示位置
     * @param gravity
     * @return
     */
    public BaseDialog<T> setGravity(int gravity) {
        this.gravity = gravity;
        isDefaultGravity = false;
        isCenter = false;
        return this;
    }

    /**
     * Dialog弹出动画效果
     * @param animator
     * @return
     */
    public BaseDialog<T> setAnimator(int animator) {
        this.animator = animator;
        isDefaultAnimator = false;
        return this;
    }

    /**
     * Dialog背景
     * @param background
     * @return
     */
    public BaseDialog<T> setBackground(int background) {
        this.background = background;
        isDefaultBackground = false;
        return this;
    }

    /**
     * Dialog矩形背景
     * @return
     */
    public BaseDialog<T> setBackgroundRectangle() {
        this.setBackground(Color.WHITE);
        return this;
    }

    /**
     * Dialog背景的灰色区域透明
     * @param isTranslucent
     * @return
     */
    public BaseDialog<T> setTranslucent(boolean isTranslucent) {
        this.isTranslucent = isTranslucent;
        return this;
    }

    /**
     * 创建并显示Dialog，放在最后调用
     * 继承时若需要设置gravity，animator， background时
     * 必须重写该方法，并且在 super.apply() 之前调用
     * 其他有关View的操作在 super.apply() 之后调用
     * @return
     */
    public BaseDialog<T> build() {
        setDialogSize();
        setStyle();
        setWindowProperty();
        show();
        afterShowSetting();
        return this;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        gravity = Gravity.CENTER;
        animator = AnimatorHelper.shrink();
        background = R.drawable.bg_radius_white;
        setCanceledOnTouchOutside(false);
        create();
    }

    /**
     * 设定一个layout
     * @return
     */
    protected abstract int layoutId();

    /**
     * 初始化View控件
     */
    protected abstract void initView();

    /**
     * 设置默认的效果
     */
    protected BaseDialog<T> setStyle() {
        if (isDefaultMargin) {
            if (gravity == Gravity.TOP) {
                if (isDefaultBackground) {
                    background = R.drawable.bg_radius_bottom_white;
                }
            } else if (gravity == Gravity.BOTTOM) {
                if (isDefaultBackground) {
                    background = R.drawable.bg_radius_top_while;
                }
            }
        }
        if (isDefaultAnimator) {
            if (gravity == Gravity.TOP) {
                animator = getTopAnimator();
            } else if (gravity == Gravity.BOTTOM) {
                animator = getBottomAnimator();
            }
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getContext()).inflate(layoutId(), null);
        setContentView(view);
        initView();
    }

    /**
     * 初始化dialog的大小
     * @return
     */
    protected BaseDialog<T> setDialogSize() {
        if (!isCenter) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.width = getDisplayWidth() - dp2px(2 * margin);
            lp.bottomMargin = dp2px(margin);
            lp.topMargin = dp2px(8 * margin);
            view.setLayoutParams(lp);
        }
        return this;
    }

    protected BaseDialog<T> setWindowProperty() {
        Window window = getWindow();
        window.setGravity(gravity);                                   // 显示的位置
        window.setWindowAnimations(animator);                         // 窗口动画
        view.setBackgroundResource(background);
        return this;
    }

    protected BaseDialog<T> afterShowSetting() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);  // 解决ALertDialog无法弹出软键盘,且必须放在AlertDialog的show方法之后
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);  // 收起键盘

        if (isTranslucent) {    // 消除Dialog内容区域外围的灰色
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                window.setDimAmount(0);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        return this;
    }

    /**
     * dialog在顶部出现的动画
     * @return
     */
    protected int getTopAnimator() {
        return AnimatorHelper.top();
    }

    /**
     * dialog在底部出现的动画
     * @return
     */
    protected int getBottomAnimator() {
        return AnimatorHelper.bottom();
    }

    /**
     * 获取dip值
     * @param value
     * @return
     */
    protected int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    protected int getDisplayWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    protected int getDisplayHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public interface OnDialogClickListener<T extends BaseDialog> {
        /**
         * @param dialog  Dialog
         * @param confirm 是否是确定按钮，通过这个判断点击的是哪个按钮
         */
        void onClick(T dialog, boolean confirm);
    }

    protected static class MyHandler extends Handler {

        WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = weakReference.get();
            if (activity == null) {
                return;
            }
        }
    }

    /**
     * 定时任务
     */
    public class TimeTask {
        private Timer mTimer;
        private TimerTask mTimerTask;
        private long mPerid;
        private long mDelay;
        private boolean reapt = false;

        /**
         * 只执行一次
         * @param perid
         * @param timerTask
         */
        public TimeTask(long perid, TimerTask timerTask) {
            this.mPerid = perid;
            this.mTimerTask = timerTask;
            this.reapt = false;
            if (mTimer == null) {
                mTimer = new Timer();
            }
        }

        /**
         * 重复执行
         * @param perid
         * @param delay
         * @param timerTask
         */
        public TimeTask(long perid, long delay, TimerTask timerTask) {
            this.mPerid = perid;
            this.mDelay = delay;
            this.reapt = true;
            this.mTimerTask = timerTask;
            if (mTimer == null) {
                mTimer = new Timer();
            }
        }

        /*
         * 启动定时任务
         */
        public TimeTask start() {
            if (reapt) {    // 执行一次所需的时间
                mTimer.schedule(mTimerTask, mDelay, mPerid);
            } else {
                mTimer.schedule(mTimerTask, mPerid);
            }
            return this;
        }

        /*
         * 取消定时任务
         */
        public TimeTask stop() {
            if (mTimer != null) {
                mTimer.cancel();
                if (mTimerTask != null) {
                    mTimerTask.cancel();                     // 将原任务从队列中移除
                }
            }
            return this;
        }
    }
}