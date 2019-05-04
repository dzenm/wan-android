package com.din.wanandroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CountdownView extends TextView implements Runnable {

    private static final String TIME_UNIT = "S";

    /*
     * 倒计时的总时间
     */
    private int mTotalTime;

    /*
     * 秒数单位文本
     */
    private String mTimeUnit;

    /*
     * 当前剩余的时间
     */
    private int mCurrentTime;

    /*
     * 记录原有的文本
     */
    private CharSequence mRecoderText;

    /*
     * 标记是否重置了倒计控件
     */
    private boolean mFlag;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTotalTime = 60;
        mFlag = false;
    }

    /**
     * 设置倒计时总秒数
     */
    public void setTotalTime(int totalTime) {
        mTotalTime = totalTime;
    }

    /**
     * 重置倒计时控件
     */
    public void reset() {
        mFlag = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 设置点击的属性
        setClickable(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this);
        super.onDetachedFromWindow();

    }

    @Override
    public boolean performClick() {
        // 点击重置事件
        mRecoderText = getText();
        setEnabled(false);
        mCurrentTime = mTotalTime;
        post(this);
        return super.performClick();
    }

    @Override
    public void run() {
        if (mCurrentTime == 0 || mFlag) {
            setText(mRecoderText);
            setEnabled(true);
            mFlag = false;
        } else {
            mCurrentTime--;
            setText(mCurrentTime + "\t" + mTimeUnit);
            postDelayed(this, 1000);
        }
    }
}
