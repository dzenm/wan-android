package com.din.helper.screen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.IntDef;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.din.helper.R;
import com.din.helper.appupdate.OsHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 屏幕获取工具类
 */
public class ScreenHelper {

    public final static int TYPE_MIUI = 0;
    public final static int TYPE_FLYME = 1;
    public final static int TYPE_M = 3;         // 6.0

    @IntDef({TYPE_MIUI, TYPE_FLYME, TYPE_M})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }

    private ScreenHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 设置toolbar,并在左上角添加动画横线按钮
     * @param activity
     * @param drawer_layout
     * @param toolbar
     */
    public static void addDrawerLayoutToggle(AppCompatActivity activity, DrawerLayout drawer_layout, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 设置toolbar,和左上角的返回按钮
     * @param activity
     * @param toolbar
     */
    public static void setToolBarAndHomeUp(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置返回按钮的点击事件
     * @param activity
     * @param item
     */
    public static void setHomeUpAction(Activity activity, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            activity.finish();
        }
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕对角线长度
     * @return
     */
    public static int getScreenDiagonal() {
        return (int) Math.sqrt(Math.pow(getScreenWidth(), 2) + Math.pow(getScreenHeight(), 2));
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, getScreenWidth(), getScreenHeight());
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight,
                getScreenWidth(), getScreenHeight() - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param colorId 颜色
     */
    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(colorId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            setTranslucentStatus(activity);
//            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
//            systemBarTintManager.setStatusBarTintEnabled(true);//显示状态栏
//            systemBarTintManager.setStatusBarTintColor(colorId);//设置状态栏颜色
        }
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }


    /**
     * 代码实现android:fitsSystemWindows
     * @param activity
     */
    public static void setRootViewFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup winContent = (ViewGroup) activity.findViewById(android.R.id.content);
            if (winContent.getChildCount() > 0) {
                ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
                if (rootView != null) {
                    rootView.setFitsSystemWindows(fitSystemWindows);
                }
            }
        }

    }


    /**
     * 设置状态栏深色浅色切换
     */
    public static boolean setStatusBarDarkTheme(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarFontIconDark(activity, TYPE_M, dark);
            } else if (OsHelper.isMiui()) {
                setStatusBarFontIconDark(activity, TYPE_MIUI, dark);
            } else if (OsHelper.isFlyme()) {
                setStatusBarFontIconDark(activity, TYPE_FLYME, dark);
            } else {//其他情况
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    public static boolean setStatusBarFontIconDark(Activity activity, @ViewType int type, boolean dark) {
        switch (type) {
            case TYPE_MIUI:
                return setMiuiUI(activity, dark);
            case TYPE_FLYME:
                return setFlymeUI(activity, dark);
            case TYPE_M:
            default:
                return setCommonUI(activity, dark);
        }
    }

    //设置6.0 状态栏深色浅色切换
    public static boolean setCommonUI(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }
                return true;
            }
        }
        return false;

    }

    //设置Flyme 状态栏深色浅色切换
    public static boolean setFlymeUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //设置MIUI 状态栏深色浅色切换
    public static boolean setMiuiUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            Class<?> clazz = activity.getWindow().getClass();
            @SuppressLint("PrivateApi") Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getDeclaredMethod("setExtraFlags", int.class, int.class);
            extraFlagField.setAccessible(true);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        int statusHeight = -1;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return statusHeight;
    }

    /**
     * dp单位转换为px单位
     * @param dpValue px值
     * @return dp单位
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * (Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    /**
     * px单位转换为dp单位
     * @param pxValue dp单位
     * @return px值
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / (Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    /**
     * EditText只能获取焦点，不弹出软键盘
     * @param editText
     */
    public static void setHideSoftInputOnFocus(EditText editText) {
        if (OsHelper.afterL()) {
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

    /**
     * dp转px
     * @param value
     * @return
     */
    public static int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}