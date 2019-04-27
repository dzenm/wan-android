package com.din.thedialog;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.TimerTask;

public class PromptDialog extends BaseDialog {

    /*
     * 延时关闭dialog的时间
     */
    private static final long DEFAULT_DELAY = 1500;

    private static final int DEFAULT_SIZE = 60;
    /*
     * 定时任务
     */
    private TimeTask timeTask;

    /*
     * dialog是否可以按返回键取消
     */
    private boolean isCancel;

    private ImageView iv_icon;

    private TextView tv_load;

    /*
     * 是否短暂的显示
     * 为true时需要自己关闭dialog
     * 为false时显示1.5秒自动关闭
     */
    private boolean isShowBrief;

    public static PromptDialog newInstance(@NonNull Context context) {
        PromptDialog promptDialog = new PromptDialog(context);
        return promptDialog;
    }

    /************************************* 以下为预定义的提示内容 *********************************/

    /**
     * 显示成功提示
     * @param success
     * @return
     */
    public PromptDialog showSuccess(String success) {
        setDialog(success, R.drawable.prompt_bg_success, true, false);
        return this;
    }

    /**
     * 显示错误提示
     * @param error
     * @return
     */
    public PromptDialog showError(String error) {
        setDialog(error, R.drawable.prompt_bg_error, true, false);
        return this;
    }

    /**
     * 显示警告提示
     * @param warming
     * @return
     */
    public PromptDialog showWarming(String warming) {
        setDialog(warming, R.drawable.prompt_bg_warming, true, false);
        return this;
    }

    /**
     * 显示刷新提示
     * @param refresh
     * @return
     */
    public PromptDialog showRefresh(String refresh) {
        setDialog(refresh, R.drawable.prompt_bg_refresh, true, false);
        return this;
    }

    /**
     * 显示加载点提示
     * @return
     */
    public PromptDialog showLoading() {
        setDialog(getContext().getResources().getString(R.string.dialog_loading), R.drawable.prompt_bg_loading_sun, false, true);
        return this;
    }

    /**
     * 显示加载提示
     * @return
     */
    public PromptDialog showLoadingPoint() {
        isShowBrief = false;
        setDialog(getContext().getResources().getString(R.string.dialog_loading), R.drawable.prompt_bg_loading_point, false, true);
        return this;
    }

    /************************************* 以下为自定义提示内容 *********************************/

    /**
     * 自定义提示文字
     * @param message
     * @return
     */
    public PromptDialog setMessage(String message) {
        tv_load.setText(message);
        return this;
    }

    /**
     * 自定义提示图片资源
     * @param icon
     * @return
     */
    public PromptDialog setImageRes(int icon) {
        setImageResourceType(icon);
        return this;
    }

    public PromptDialog setDialog(String t, int ic, boolean brief, boolean cancel) {
        tv_load.setText(t);
        setImageResourceType(ic);
        isShowBrief = brief;
        isCancel = cancel;
        build();
        return this;
    }

    /************************************* 以下为实现细节（不可见方法） *********************************/

    protected PromptDialog(@NonNull Context context) {
        super(context);
        this.background = R.drawable.bg_radius_blackff;
        this.animator = AnimatorHelper.alpha();
        this.isCancel = true;
    }

    @Override
    public BaseDialog build() {
        super.build();
        setCancelable(isCancel);                                        // 设置不可通过返回键取消dialog
        if (isShowBrief) {                                              // 是否简短的显示之后取消
            if (timeTask == null) {
                timeTask = new TimeTask(DEFAULT_DELAY, new TimerTask() {
                    @Override
                    public void run() {
                        //在此执行定时操作
                        timeTask.stop();
                        PromptDialog.this.dismiss();
                    }
                });
                timeTask.start();
            }
        }
        return this;
    }

    @Override
    public PromptDialog setGravity(int gravity) {
        super.setGravity(gravity);
        isDefaultBackground = false;
        return this;
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_prompt;
    }

    @Override
    protected void initView() {
        iv_icon = findViewById(R.id.iv_icon);
        tv_load = findViewById(R.id.tv_load);
    }

    /**
     * 设置不同类型的图片
     * @param icon
     */
    protected void setImageResourceType(int icon) {
        ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
        layoutParams.width = dp2px(DEFAULT_SIZE);
        layoutParams.height = dp2px(DEFAULT_SIZE);
        iv_icon.setLayoutParams(layoutParams);
        iv_icon.setImageResource(icon);
        ((Animatable) iv_icon.getDrawable()).start();
    }

    @Override
    protected PromptDialog setDialogSize() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = dp2px(margin);
        layoutParams.topMargin = dp2px(8 * margin);
        view.setLayoutParams(layoutParams);
        return this;
    }
}