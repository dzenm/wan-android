package com.din.helper.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.din.helper.draw.DrawableHelper;
import com.din.helper.R;

/**
 * @author dinzhenyan
 * @date 2019-05-13 22:24
 * @IDE Android Studio
 */
public class LoginDialog extends AbsDialog implements View.OnClickListener {

    private static final int TYPE_LOGIN = 1;
    private static final int TYPE_REGISTER = 2;

    private static final int TYPE_LOGIN_BY_PASSWORD = 2;
    private static final int TYPE_LOGIN_BY_VERIFE = 4;

    private static final int DEFAULT_TIME = 60;

    /*
     * 控件
     */
    private TextView tvLogin, tvRegister, tvCountdown;
    private EditText etUsername, etPassword, etVerifeCode;
    private Button btnConfirm, btnCancel;
    private LinearLayout llVerife;

    /*
     * 登录的类型，可选值有(密码登录，验证码登录)
     */
    private int mLoginType;

    /*
     * 倒计时的总时间，默认时间为60秒
     */
    private int mCountTime;

    /*
     * 总体颜色显示，默认为蓝色
     */
    private int mColor;

    /*
     * 点击事件
     */
    private OnClickListener onClickListener;

    public LoginDialog setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public LoginDialog setLoginByPassword() {
        mLoginType = TYPE_LOGIN_BY_PASSWORD;
        return this;
    }

    public LoginDialog setLoginByVerifeCode() {
        mLoginType = TYPE_LOGIN_BY_VERIFE;
        return this;
    }

    @Override
    public AbsDialog build() {
        super.build();
        setLoginType(false);
        return this;
    }

    /************************************* 以下为实现细节（不可见方法）**********************************/
    /**
     * @param context
     */
    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_login;
    }

    @Override
    protected void initView() {
        tvLogin = findViewById(R.id.tv_login);
        tvRegister = findViewById(R.id.tv_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        llVerife = findViewById(R.id.ll_verife);
        tvCountdown = findViewById(R.id.tv_countdown);
        etVerifeCode = findViewById(R.id.et_verife_code);

        btnConfirm = findViewById(R.id.bt_confirm);
        btnCancel = findViewById(R.id.bt_cancel);

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvCountdown.setOnClickListener(this);

        mCountTime = DEFAULT_TIME;
        mColor = getColor(R.color.colorBlue);
        setFlatStyle(tvLogin, tvRegister, tvCountdown, etUsername, etPassword,
                etVerifeCode, btnConfirm, btnCancel);

    }

    /**
     * 设置样式
     * @param tvLogin
     * @param tvRegister
     * @param tvCountdown
     * @param etUsername
     * @param etPassword
     * @param etVerifeCode
     * @param btnConfirm
     * @param btnCancel
     */
    protected void setFlatStyle(TextView tvLogin, TextView tvRegister, TextView tvCountdown,
                                EditText etUsername, EditText etPassword, EditText etVerifeCode,
                                Button btnConfirm, Button btnCancel) {
        tvLogin.setBackground(getLeftRadius());
        tvLogin.setTextColor(Color.WHITE);
        tvRegister.setBackground(getRightRadiusBlueStroke());
        tvRegister.setTextColor(mColor);
        etUsername.setBackgroundResource(R.drawable.bg_gray_border);
        etPassword.setBackgroundResource(R.drawable.bg_gray_border);
        etVerifeCode.setBackgroundResource(R.drawable.bg_gray_border);
        tvCountdown.setBackgroundColor(mColor);
        tvCountdown.setTextColor(Color.WHITE);
        btnConfirm.setBackground(null);
        btnCancel.setBackground(null);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            if (v.getId() == R.id.bt_confirm) {             // 确认按钮
                onClickListener.onClick(LoginDialog.this);
            } else if (v.getId() == R.id.bt_cancel) {       // 取消按钮
                dismiss();
            } else if (v.getId() == R.id.tv_login) {        // 登录按钮（切换Tab）
                cancelCountdown();
                setTagSelected(TYPE_LOGIN);
                setLoginType(false);
            } else if (v.getId() == R.id.tv_register) {     // 注册按钮（切换Tab）
                cancelCountdown();
                setTagSelected(TYPE_REGISTER);
                setLoginType(true);
            } else if (v.getId() == R.id.tv_countdown) {    // 倒计时按钮
                // 倒计时按钮变灰并不可点击
                tvCountdown.setEnabled(false);
                tvCountdown.setBackgroundColor(getColor(R.color.colorDivide));
                // 倒计时开始
                downTimer.start();
                // 请求验证码
                onClickListener.onVerifeClick();
            }
        } else {
            throw new NullPointerException("onClickListener is null");
        }
    }

    private CountDownTimer downTimer = new CountDownTimer(mCountTime * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvCountdown.setText(millisUntilFinished / 1000 + getString(R.string.dialog_second));
        }

        @Override
        public void onFinish() {
            reset();
        }
    };

    /**
     * 重新获取
     */
    private void reset() {
        // 倒计时结束时可重新点击获取并设置显式的可点击的颜色以及提示文字
        tvCountdown.setEnabled(true);
        tvCountdown.setBackgroundColor(mColor);
        tvCountdown.setText(getString(R.string.dialog_reset_countdown));
    }

    /**
     * 取消倒计时
     */
    private void cancelCountdown() {
        downTimer.cancel();
        reset();
    }

    /**
     * 登录的方式
     */
    private void setLoginType(boolean visible) {
        if (mLoginType == TYPE_LOGIN_BY_PASSWORD) {
            llVerife.setVisibility(visible ? View.VISIBLE : View.GONE);
        } else if (mLoginType == TYPE_LOGIN_BY_VERIFE) {
            etPassword.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 选中的标题类型
     */
    private void setTagSelected(int tag) {
        if (tag == TYPE_LOGIN) {
            tvLogin.setBackground(getLeftRadius());
            tvLogin.setTextColor(Color.WHITE);
            tvRegister.setBackground(getRightRadiusBlueStroke());
            tvRegister.setTextColor(mColor);
        } else if (tag == TYPE_REGISTER) {
            tvLogin.setBackground(getLeftRadiusBlueStroke());
            tvLogin.setTextColor(mColor);
            tvRegister.setBackground(getRightRadius());
            tvRegister.setTextColor(Color.WHITE);
        }
    }

    /**
     * 蓝色背景，左上圆角
     * @return
     */
    public GradientDrawable getLeftRadius() {
        return DrawableHelper.getDrawable(getColor(R.color.colorBlue), new float[]{8, 0, 0, 0});
    }

    /**
     * 蓝色背景，右上圆角
     * @return
     */
    public GradientDrawable getRightRadius() {
        return DrawableHelper.getDrawable(getColor(R.color.colorBlue), new float[]{0, 8, 0, 0});
    }

    /**
     * 白色背景，左上圆角，蓝色边框
     * @return
     */
    public GradientDrawable getLeftRadiusBlueStroke() {
        return DrawableHelper.getDrawable(Color.WHITE, new float[]{8, 0, 0, 0}, 1, getColor(R.color.colorBlue));
    }

    /**
     * 白色背景，右上圆角，蓝色边框
     * @return
     */
    public GradientDrawable getRightRadiusBlueStroke() {
        return DrawableHelper.getDrawable(Color.WHITE, new float[]{0, 8, 0, 0}, 1, getColor(R.color.colorBlue));
    }

    public interface OnClickListener {

        void onClick(LoginDialog dialog);

        void onVerifeClick();
    }
}
