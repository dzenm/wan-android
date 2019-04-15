package com.din.helper.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

public class PackageHelper {

    /**
     * 返回应用程序在清单文件中注册的权限
     *
     * @param activity
     * @return
     */
    public static List<String> getManifestPermissions(Activity activity) {
        try {
            return Arrays.asList(activity.getPackageManager().getPackageInfo(activity.getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否有安装权限
     *
     * @param activity
     * @return
     */
    public static boolean isInstallPermission(Activity activity) {
        if (isOreo()) {
            return activity.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 是否有悬浮窗权限
     *
     * @param activity
     * @return
     */
    public static boolean isOverlaysPermission(Activity activity) {
        if (isMarshmallow()) {
            return Settings.canDrawOverlays(activity);
        }
        return true;
    }

    /**
     * 检查是否授予权限
     *
     * @param permission
     * @return
     */
    public static boolean isGrant(Activity activity, String permission) {
        return (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 检测权限是否在清单文件中注册
     *
     * @param activity
     * @param permission
     */
    public static boolean isExistInManifest(Activity activity, String permission) {
        List<String> manifestPermissions = getManifestPermissions(activity);
        if (manifestPermissions == null && manifestPermissions.isEmpty()) {
            throw new NullPointerException("AndroidManifest permission is null");
        }
        if (manifestPermissions.equals(permission)) {
            return true;
        } else {
            throw new NullPointerException("AndroidManifest's permission is not found");
        }
    }

    /**
     * 是否显示解释权限
     *
     * @param permission
     * @return
     */
    public static boolean isRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 动态权限只有在Android 6.0之后
     *
     * @return
     */
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否是8.0以上版本
     *
     * @return
     */
    public static boolean isOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

}
