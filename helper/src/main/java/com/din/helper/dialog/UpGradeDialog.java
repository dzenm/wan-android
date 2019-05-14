package com.din.helper.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.din.helper.R;
import com.din.helper.draw.DrawableHelper;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 更新提示的Dialog
 */
public class UpGradeDialog extends AbsDialog implements View.OnClickListener, LoadProgress.OnLoadListener {

    /*
     * 顶部的图片
     */
    private int headerImage;

    private TextView tvVersion;
    private TextView tvSize;
    private TextView tvDesc;
    private ImageView ivHead;

    /*
     * 下载的进度条
     */
    private LoadProgress mLoadProgress;

    /*
     * 升级事件
     */
    private OnUpGradeListener onUpGradeListener;

    public static UpGradeDialog newInstance(@NonNull Context context) {
        UpGradeDialog upGradeDialog = new UpGradeDialog(context);
        return upGradeDialog;
    }

    /************************************* 以下为自定义提示内容 *********************************/

    /**
     * 设置版本
     * @param version
     * @return
     */
    public UpGradeDialog setVersion(String version) {
        tvVersion.setText(version);
        return this;
    }

    /**
     * 设置安装包大小
     * @param size
     * @return
     */
    public UpGradeDialog setSize(String size) {
        tvSize.setText(size);
        return this;
    }

    /**
     * 设置升级的描述信息
     * @param desc
     * @return
     */
    public UpGradeDialog setDesc(String desc) {
        tvDesc.setText(desc);
        return this;
    }

    /**
     * 升级事件
     * @param onUpGradeListener
     * @return
     */
    public UpGradeDialog setOnUpGradeListener(OnUpGradeListener onUpGradeListener) {
        this.onUpGradeListener = onUpGradeListener;
        return this;
    }

    /************************************* 以下为实现细节（不可见方法）***********************************/

    protected UpGradeDialog(@NonNull Context context) {
        super(context);
        mBackground = DrawableHelper.getDrawable(Color.TRANSPARENT, 0);
        mAnimator = AnimatorHelper.overshoot();
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_up_grade;
    }

    @Override
    protected void initView() {
        ivHead = findViewById(R.id.iv_header);
        tvVersion = findViewById(R.id.tv_version);
        tvSize = findViewById(R.id.tv_size);
        tvDesc = findViewById(R.id.tv_desc);
        mLoadProgress = findViewById(R.id.load_progress);

        Button bt_upgrade = findViewById(R.id.bt_upgrade);
        ImageView iv_cancel = findViewById(R.id.iv_cancel);
        bt_upgrade.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    @Override
    protected AbsDialog setDialogLayoutParams(ViewGroup.MarginLayoutParams lp) {
        lp.width = (int) (getDisplayWidth() * 0.7);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_upgrade) {
            v.setVisibility(View.GONE);
            mLoadProgress.setVisibility(View.VISIBLE);
            mLoadProgress.setOnLoadListener(this);
            onUpGradeListener.onStart(mLoadProgress);
        } else if (v.getId() == R.id.iv_cancel) {
            dismiss();
        }
    }

    @Override
    public void onFinished() {
        dismiss();
        onUpGradeListener.onFinished();
    }

    public interface OnUpGradeListener {

        void onStart(LoadProgress loadProgress);

        void onFinished();
    }
}
