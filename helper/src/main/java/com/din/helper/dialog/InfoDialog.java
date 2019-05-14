package com.din.helper.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.din.helper.R;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 提示的Dialog
 */
public class InfoDialog extends AbsDialog implements View.OnClickListener {

    /*
     * 提示框的标题
     */
    private TextView tvInfo;

    /*
     * 提示框的内容
     */
    protected EditText etContent;

    /*
     * 提示框可点击的按钮
     */
    protected Button btPositive, btNegative;

    private OnDialogClickListener onDialogClickListener;

    public static InfoDialog newInstance(@NonNull Context context) {
        InfoDialog infoDialog = new InfoDialog(context);
        return infoDialog;
    }

    /************************************* 以下为自定义提示内容 *********************************/

    /**
     * 设置提示的标题
     * @param info
     * @return
     */
    public InfoDialog setInfo(String info) {
        if (!TextUtils.isEmpty(info)) {
            tvInfo.setText(info);
        }
        tvInfo.setVisibility(TextUtils.isEmpty(info) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置提示的内容
     * @param content
     * @return
     */
    public InfoDialog setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
        }
        etContent.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 获取输入的文本
     * @return
     */
    public String getContent() {
        return etContent.getText().toString();
    }

    /**
     * 设置按钮的文本
     * @param positiveText
     * @param negativeText
     * @return
     */
    public InfoDialog setButtonText(String positiveText, String negativeText) {
        if (!TextUtils.isEmpty(positiveText)) {
            btPositive.setText(positiveText);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            btNegative.setText(negativeText);
        }
        return this;
    }

    /**
     * 设置点击事件
     * @param onDialogClickListener
     * @return
     */
    public InfoDialog setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
        return this;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    protected InfoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_info;
    }

    @Override
    protected void initView() {
        tvInfo = findViewById(R.id.tv_info);
        etContent = findViewById(R.id.et_content);
        btPositive = findViewById(R.id.bt_positive);
        btNegative = findViewById(R.id.bt_negative);

        btPositive.setOnClickListener(this);
        btNegative.setOnClickListener(this);

        setEditBehavior(etContent);
    }

    /**
     * 设置EditText行为
     * @param etContent
     */
    protected void setEditBehavior(EditText etContent) {
        etContent.setBackground(null);
        etContent.setFocusable(false);
        etContent.setFocusableInTouchMode(false);
        etContent.setOnTouchListener(null);
        etContent.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        if (onDialogClickListener != null) {
            if (v.getId() == R.id.bt_positive) {
                onDialogClickListener.onClick(InfoDialog.this, true);
            } else if (v.getId() == R.id.bt_negative) {
                onDialogClickListener.onClick(InfoDialog.this, false);
            }
            dismiss();
        } else {
            throw new NullPointerException("onDialogClickListener is null");
        }
    }
}