package com.din.helper.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.din.helper.R;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 带输入的Dialog
 */
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
    protected void setEditBehavior(EditText etContent) {
        etContent.requestFocus();                              // 获取焦点
        etContent.setHint(R.string.dialog_edit_hint);                         // 设置提示文字
        etContent.setSelection(etContent.getText().length());  // 如果有内容将光标移到最后面
        etContent.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etContent.getText().toString().trim().length() == 0) { // 监听输入框，没有内容时设置提示文字
            etContent.setHint(R.string.dialog_edit_hint);
            etContent.setEnabled(false);
        } else {
            etContent.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}