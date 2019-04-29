package com.din.helper.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.din.helper.R;

public class ListDialog extends BaseDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    private TextView tv_title;
    private Button bt_cancel;
    private ListView lv_list;
    private ListAdapter adapter;
    private OnItemClickListener onItemClickListener;

    private String[] mTexts;
    private int[] mIcons;
    private boolean isIcon;

    /************************************* 以下为自定义提示内容 *********************************/

    public static ListDialog newInstance(@NonNull Context context) {
        ListDialog listDialog = new ListDialog(context);
        return listDialog;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public ListDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置数据
     * @param texts
     * @return
     */
    public ListDialog setData(String[] texts) {
        mTexts = texts;
        isIcon = false;
        return this;
    }

    public ListDialog setData(String[] texts, int[] icons) {
        mTexts = texts;
        mIcons = icons;
        isIcon = true;
        return this;
    }

    public ListDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    protected ListDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public BaseDialog build() {
        mAnimator = AnimatorHelper.shrink();
        super.build();
        lv_list.setBackgroundResource(mBackground);
        mView.setBackground(null);                  // 设置不必要的背景
        if (isIcon) {                               // 设置数据
            adapter.setData(mTexts, mIcons);
        } else {
            adapter.setData(mTexts);
        }
        if (isDefaultGravity) {                     // 覆盖父类的设置，重新设置背景
            bt_cancel.setBackgroundResource(R.drawable.bg_pressed_radius_white);
        } else {
            bt_cancel.setBackgroundColor(Color.WHITE);
        }
        lv_list.setOnItemClickListener(this);
        lv_list.setAdapter(adapter);
        return this;
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_list;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 重新设置dialog的大小，设定一个最大高度
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        int height = window.getDecorView().getHeight();
        int maxHeight = (int) (getDisplayHeight() * 0.6);
        if (height > maxHeight) {                                   // 判断当前高度是否高于最大高度
            layoutParams.height = (int) (getDisplayHeight() * 0.6); // 重设高度为屏幕高度的60%
        }
        window.setAttributes(layoutParams);
    }

    @Override
    protected void initView() {
        lv_list = findViewById(R.id.lv_list);
        tv_title = findViewById(R.id.tv_title);
        bt_cancel = findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        adapter = new ListAdapter(getContext());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_cancel) {
            this.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position);
            dismiss();
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}