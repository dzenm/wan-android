package com.din.helper.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public final class PermissionManager {

    public static final int REQUEST_CODE = 1;

    /*
     * 每次打开软件对未授予的权限只提示一次请求权限，不管授予权限还是未授予权限，onPermissionCallback都会返回true
     */
    public static final int TYPE_REQUEST_ONCE = 1;

    /*
     * 对请求的权限一定要授予权限才可以使用，否则重复请求
     */
    public static final int TYPE_REQUESY_REPEAT = 2;

    /*
     * 每次打开软件对未授予的权限提示一次请求权限，如果未授予则会提示手动打开。
     */
    public static final int TYPE_REQUEST_ONCE_AND_MANUAL = 3;

    private int requestMode = TYPE_REQUEST_ONCE_AND_MANUAL;     // 请求权限的模式
    private Activity activity;
    private String[] permissions;                               // 需要请求的权限
    private OnPermissionListener onPermissionListener;

    private PermissionManager() {
    }

    private static class Instance {
        private static final PermissionManager INSTANCE = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return Instance.INSTANCE;
    }

    /**
     * 设置activity
     *
     * @param activity
     * @return
     */
    public PermissionManager with(Activity activity) {
        this.activity = activity;
        return this;
    }

    /**
     * 请求权限模式
     *
     * @param requestMode
     * @return
     */
    public PermissionManager mode(int requestMode) {
        this.requestMode = requestMode;
        return this;
    }

    /**
     * 请求的权限列表
     *
     * @param permissions
     * @return
     */
    public PermissionManager load(String[] permissions) {
        this.permissions = onFilterPermission(permissions);                  // 过滤未授予的请求
        return this;
    }

    /**
     * 权限请求回掉处理
     *
     * @param onPermissionListener
     * @return
     */
    public PermissionManager into(OnPermissionListener onPermissionListener) {
        this.onPermissionListener = onPermissionListener;
        return this;
    }

    /**
     * 请求权限
     *
     * @return
     */
    public PermissionManager requestPermission() {
        if (PackageHelper.isMarshmallow()) {
            if (permissions.length < 0) {                                       // 处理请求的结果
                requestSuccess();
            } else {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
            }
        }
        return this;
    }

    /**
     * 权限处理结果（重写onRequestPermissionsResult方法，并调用该方法）
     *
     * @param permissions
     * @param grantResults
     */
    public PermissionManager requestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionManager.REQUEST_CODE != requestCode) {
            return this;
        }
        String[] grantPermissions = onFilterPermission(permissions);   // 第一次请求的处理结果，过滤已授予的权限
        boolean isGrant = true;
        for (int i = 0; i < grantResults.length; i++) {                // 是否所有的权限都被授予
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isGrant = false;
                break;
            }
        }

        if (grantPermissions.length == 0 && isGrant) {                 // 是否还存在未授予的权限
            requestSuccess();
        } else if (requestMode == TYPE_REQUEST_ONCE) {                 // 是否对于未授予的权限重复请求
            openFaildDialog("未授予软件运行需要的权限");
        } else if (requestMode == TYPE_REQUEST_ONCE_AND_MANUAL) {
            openSettingDialog("未授予相应的权限，可能会导致软件崩溃。请前往设置授予相应的权限");
        } else if (requestMode == TYPE_REQUESY_REPEAT) {
            load(permissions).requestPermission();
        }
        return this;
    }

    /**
     * 权限请求成功
     */
    private PermissionManager requestSuccess() {
        onPermissionListener.onPermit(true);
//        FileHelper.getInstance().init(activity);                        // 文件夹初始化
//        LogHelper.getInstance().start();                                // Logcat初始化
        return this;
    }

    /**
     * 过滤未授予的权限
     *
     * @param permissions
     * @return
     */
    private String[] onFilterPermission(String[] permissions) {
        List<String> filterPermits = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (PackageHelper.isGrant(activity, permissions[i])) {
                filterPermits.add(permissions[i]);
            }
        }
        return filterPermits.toArray(new String[filterPermits.size()]);
    }

    /**
     * 打开设置权限的对话框
     *
     * @param message
     */
    private PermissionManager openSettingDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("温馨提示");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MobileSetting.openSetting(activity, true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPermissionListener.onPermit(false);
                dialog.dismiss();
            }
        });
        builder.create().show();
        return this;
    }

    /**
     * 打开未授予权限的对话框
     *
     * @param message
     */
    private PermissionManager openFaildDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("温馨提示");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestSuccess();
                dialog.dismiss();
            }
        });
        builder.create().show();
        return this;
    }

    public interface OnPermissionListener {

        void onPermit(boolean isGrant);
    }
}
