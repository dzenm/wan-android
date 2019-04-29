package com.din.helper.util;

/**
 * @author dinzhenyan
 * @date 2019-04-29 20:12
 * @IDE Android Studio
 */
public interface UpdateAPPImpl {

    /**
     * 检验版本
     * @return 是否需要更新
     */
    boolean verifyVersion();

    /**
     * 显示更新提示的dialog
     */
    void showUpdateDialog();

    /**
     * 下载APK文件
     */
    void downloadAPK();

    /**
     * 安装APK
     * @param file
     */
    void installAPK(String file);


    /**
     * 显示下载通知
     */
    void showNotifitation();
}
