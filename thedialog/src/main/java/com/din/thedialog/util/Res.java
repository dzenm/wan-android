package com.din.thedialog.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Res {

    /**
     * 系统版本是否在Android 5.0之后
     * @return
     */
    public static boolean afterL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 系统版本是否在Android 7.0之后
     * @return
     */
    public static boolean afterN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 系统版本是否在Android 8.0之后
     * @return
     */
    public static boolean afterO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 获取dip值
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽地
     * @return
     */
    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏宽的百分比
     * @param value
     * @return
     */
    public static int getDisplayWidth(Context context, float value) {
        return (int) (context.getResources().getDisplayMetrics().widthPixels * value);
    }

    /**
     * EditText只能获取焦点，不弹出软键盘
     * @param editText
     */
    public static void setHideSoftInputOnFocus(EditText editText) {
        if (Res.afterL()) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            Class<EditText> editClass = EditText.class;
            try {
                Method method = editClass.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, editClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示软键盘
     * @param editText
     * @return
     */
    public static boolean showSoftInput(EditText editText) {
        return ((InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
    }

    /**
     * 隐藏软键盘
     * @param editText
     * @return
     */
    public static boolean hideSoftInput(EditText editText) {
        return ((InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
