package com.din.helper.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialog;
import com.din.helper.R;
import com.din.helper.draw.DrawableHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * Dialog的抽象类
 */
public abstract class AbsDialog extends AppCompatDialog {

    /*
     * root view
     */
    protected View mView;

    /*
     * dialog背景，可通过setBackground()方法设置background
     * 默认的background在构造方法里，为白色圆角背景
     */
    protected GradientDrawable mBackground;

    /*
     * dialog边距值， 可通过setMargin()方法设置margin
     * margin的值为上下边距，左右边距是上下边距的两倍
     */
    protected int mMargin;

    /*
     * dialog位置，可通过setGravity设置gravity，默认显示在中间
     */
    protected int mGravity;

    /*
     * dialog动画，根据gravity的位置显示动画，自定义动画通过setAnimator设置
     * dialog显示在顶部时从顶部往下弹出
     * dialog显示在底部时从底部往上弹出
     * dialog显示在中间时从中间缩放显示
     */
    protected int mAnimator;

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
     * 去除dialog灰色区域（即背景透明）
     */
    protected boolean isTranslucent = false;

    /*
     * dialog显示在中间
     */
    private boolean isCenter = true;

    /*
     * 默认的dialog边距
     */
    private int mDefaultMargin = 10;

    /*
     * 默认全圆角显示
     */
    private boolean isDefaultRadius = true;

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
    public AbsDialog setMargin(int margin) {
        mMargin = margin;
        isDefaultMargin = false;
        return this;
    }

    /**
     * Dialog显示位置
     * @param gravity
     * @return
     */
    public AbsDialog setGravity(int gravity) {
        mGravity = gravity;
        isDefaultGravity = false;
        isCenter = false;
        return this;
    }

    /**
     * Dialog弹出动画效果
     * @param animator
     * @return
     */
    public AbsDialog setAnimator(int animator) {
        mAnimator = animator;
        isDefaultAnimator = false;
        return this;
    }

    /**
     * Dialog背景
     * @param background
     * @return
     */
    public AbsDialog setBackground(GradientDrawable background) {
        mBackground = background;
        isDefaultBackground = false;
        return this;
    }

    /**
     * Dialog矩形背景
     * @return
     */
    public AbsDialog setBackgroundRectangle() {
        setBackground(DrawableHelper.getDrawable(Color.WHITE, 0));
        return this;
    }

    /**
     * Dialog背景的灰色区域透明
     * @param translucent
     * @return
     */
    public AbsDialog setTranslucent(boolean translucent) {
        isTranslucent = translucent;
        return this;
    }

    /**
     * 点击dialog之外是否可以取消
     * @param cancel
     * @return
     */
    public AbsDialog setTouchOutsideCancel(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 创建并显示Dialog，放在最后调用
     * 继承时若需要设置gravity，animator， background时
     * 必须重写该方法，并且在 super.apply() 之前调用
     * 其他有关View的操作在 super.apply() 之后调用
     * @return
     */
    public AbsDialog build() {
        Window window = getWindow();
        setDialogSize(window);
        setStyle();
        setWindowProperty(window);
        show();
        afterShowSetting(window);
        return this;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    public AbsDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public AbsDialog(Context context, int theme) {
        super(context, theme);
        mGravity = Gravity.CENTER;
        mAnimator = AnimatorHelper.shrink();
        mBackground = getRadius();
        mMargin = mDefaultMargin;
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
    protected AbsDialog setStyle() {
        if (!isDefaultRadius) {
            if (isDefaultMargin) {
                if (mGravity == Gravity.TOP) {
                    if (isDefaultBackground) {
                        mBackground = getBottomRadius();
                    }
                } else if (mGravity == Gravity.BOTTOM) {
                    if (isDefaultBackground) {
                        mBackground = getTopRadius();
                    }
                }
            }
        }
        if (isDefaultAnimator) {
            if (mGravity == Gravity.TOP) {
                mAnimator = AnimatorHelper.top();
            } else if (mGravity == Gravity.BOTTOM) {
                mAnimator = AnimatorHelper.bottom();
            }
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(layoutId(), null);
        setContentView(mView);
        initView();
    }

    /**
     * 供子类查找id
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    /**
     * 初始化dialog的大小
     * @return
     */
    private AbsDialog setDialogSize(Window window) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        if (isCenter) {
            setCenterDialogLayoutParams(windowAttributes);
        } else {
            setDialogLayoutParams(layoutParams);
            mView.setLayoutParams(layoutParams);
        }
        window.setAttributes(windowAttributes);
        return this;
    }

    /**
     * 设置dialog的LayoutParams
     * @return
     */
    protected AbsDialog setDialogLayoutParams(ViewGroup.MarginLayoutParams layoutParams) {
        layoutParams.topMargin = dp2px(mMargin);
        layoutParams.bottomMargin = dp2px(mMargin);
        layoutParams.width = getDisplayWidth() - 2 * dp2px(mMargin);
        return this;
    }

    /**
     * 设置dialog居中的LayoutParams
     * @return
     */
    protected AbsDialog setCenterDialogLayoutParams(WindowManager.LayoutParams windowAttributes) {
        if (!isPromptDialog()) {
            windowAttributes.width = getDisplayWidth() - 10 * dp2px(mMargin);
        }
        return this;
    }

    protected boolean isPromptDialog() {
        return false;
    }

    /**
     * 设置Windows的属性
     * @return
     */
    protected AbsDialog setWindowProperty(Window window) {
        window.setGravity(mGravity);                                  // 显示的位置
        window.setWindowAnimations(mAnimator);                        // 窗口动画
        mView.setBackground(mBackground);
        return this;
    }

    /**
     * Dialog调用show方法之后的一些设置
     * @return
     */
    protected AbsDialog afterShowSetting(Window window) {
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
     * 获取字符串
     * @param id
     * @return
     */
    protected String getString(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 获取颜色值
     * @param id
     * @return
     */
    protected int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    /**
     * 获取屏幕高度
     * @return
     */
    protected int getDisplayHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 圆角，白色背景
     * @return
     */
    public GradientDrawable getRadius() {
        return DrawableHelper.getDrawable(8);
    }

    /**
     * 顶部圆角，白色背景
     * @return
     */
    public GradientDrawable getTopRadius() {
        return DrawableHelper.getDrawable(new float[]{8, 8, 0, 0});
    }

    /**
     * 底部圆角，白色背景
     * @return
     */
    public GradientDrawable getBottomRadius() {
        return DrawableHelper.getDrawable(new float[]{0, 0, 8, 8});
    }

    /**
     * 圆角，背景透明灰
     * @return
     */
    public GradientDrawable getRadiusGrayTranslucent() {
        return DrawableHelper.getDrawable(getColor(R.color.colorPrimaryTranculent), 8);
    }

    public GradientDrawable getEditStroke() {
        return DrawableHelper.getDrawable(Color.TRANSPARENT, 2, 1, getColor(R.color.colorDivide));
    }

    public interface OnDialogClickListener<T extends AbsDialog> {
        /**
         * @param dialog  Dialog
         * @param confirm 是否是确定按钮，通过这个判断点击的是哪个按钮
         */
        void onClick(T dialog, boolean confirm);
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