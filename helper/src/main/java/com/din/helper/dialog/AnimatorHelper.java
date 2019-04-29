package com.din.helper.dialog;

import com.din.helper.R;

public final class AnimatorHelper {

    /**
     * 放大
     *
     * @return
     */
    public static int expand() {
        return R.style.BaseDialog_Scale_Expand_Animator;
    }

    /**
     * 缩小
     *
     * @return
     */
    public static int shrink() {
        return R.style.BaseDialog_Scale_Shrink_Animator;
    }

    /**
     * 下进下出
     *
     * @return
     */
    public static int bottom() {
        return R.style.BaseDialog_Bottom_Animator;
    }

    /**
     * 下进上出
     *
     * @return
     */
    public static int bottom2Top() {
        return R.style.BaseDialog_Bottom_Top_Animator;
    }

    /**
     * 上进上出
     *
     * @return
     */
    public static int top() {
        return R.style.BaseDialog_Top_Animator;
    }

    /**
     * 上进下出
     *
     * @return
     */
    public static int top2Bottom() {
        return R.style.BaseDialog_Top_Bottom_Animator;
    }

    /**
     * 左进左出
     *
     * @return
     */
    public static int left() {
        return R.style.BaseDialog_Left_Animator;
    }

    /**
     * 右进右出
     *
     * @return
     */
    public static int right() {
        return R.style.BaseDialog_Right_Animator;
    }

    /**
     * 左进右出
     *
     * @return
     */
    public static int left2Right() {
        return R.style.BaseDialog_Left_Right_Animator;
    }

    /**
     * 右进左出
     *
     * @return
     */
    public static int right2Left() {
        return R.style.BaseDialog_Right_Left_Animator;
    }

    /**
     * 下弹出上弹出
     *
     * @return
     */
    public static int overshoot() {
        return R.style.BaseDialog_Overshoot_Animator;
    }
    /**
     * 透明度变化
     *
     * @return
     */
    public static int alpha() {
        return R.style.BaseDialog_Alpha_Animator;
    }

}
