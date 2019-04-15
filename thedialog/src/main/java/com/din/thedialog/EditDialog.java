package com.din.thedialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class EditDialog extends InfoDialog implements TextWatcher {

    /************************************* 以下为自定义提示内容 *********************************/

    public static EditDialog newInstance(@NonNull Context context) {
        EditDialog editDialog = new EditDialog(context);
        return editDialog;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    protected EditDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void setEditBehavior(EditText et_content) {
        et_content.requestFocus();                              // 获取焦点
        et_content.setHint("请输入内容");                         // 设置提示文字
        et_content.setSelection(et_content.getText().length()); // 如果有内容将光标移到最后面
        et_content.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_content.getText().toString().trim().length() == 0) { // 监听输入框，没有内容时设置提示文字
            et_content.setHint("请输入内容");
            bt_positive.setEnabled(false);
        } else {
            bt_positive.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}