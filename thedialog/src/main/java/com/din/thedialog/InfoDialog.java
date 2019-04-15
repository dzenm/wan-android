package com.din.thedialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class InfoDialog extends BaseDialog implements View.OnClickListener {

    private TextView tv_info;
    protected EditText et_content;
    protected Button bt_positive, bt_negative;
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
            tv_info.setText(info);
        }
        tv_info.setVisibility(TextUtils.isEmpty(info) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置提示的内容
     * @param content
     * @return
     */
    public InfoDialog setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            et_content.setText(content);
        }
        return this;
    }

    /**
     * 获取输入的文本
     * @return
     */
    public String getContent() {
        return et_content.getText().toString();
    }

    /**
     * 设置左边的按钮的文本
     * @param positiveText
     * @param negativeText
     * @return
     */
    public InfoDialog setButtonText(String positiveText, String negativeText) {
        if (!TextUtils.isEmpty(positiveText)) {
            bt_positive.setText(positiveText);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            bt_negative.setText(negativeText);
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
        tv_info = findViewById(R.id.tv_info);
        et_content = findViewById(R.id.et_content);
        bt_positive = findViewById(R.id.bt_positive);
        bt_negative = findViewById(R.id.bt_negative);

        bt_positive.setOnClickListener(this);
        bt_negative.setOnClickListener(this);

        setEditBehavior(et_content);
    }

    /**
     * 设置不可点击
     * @param et_content
     */
    protected void setEditBehavior(EditText et_content) {
        et_content.setBackground(null);
        et_content.setFocusable(false);
        et_content.setFocusableInTouchMode(false);
        et_content.setOnTouchListener(null);
        et_content.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_positive) {
            onDialogClickListener.onClick(InfoDialog.this, true);
        } else if (v.getId() == R.id.bt_negative) {
            onDialogClickListener.onClick(InfoDialog.this, false);
        }
        dismiss();
    }
}