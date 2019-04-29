package com.din.helper.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.din.helper.file.FileHelper;

import java.lang.ref.WeakReference;

public abstract class AbsUpdateAPP implements UpdateAPPImpl {

    protected Activity sActivity;
    private static final String HEAD = "file://";
    private static final String TYPE = "application/vnd.android.package-archive";
    protected String downloadUrl;


    /**
     * 下载完成安装apk，给系统发送一个intent
     * @return
     */
    public AbsUpdateAPP openAPKIntent() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(HEAD + getFileSavePath()), TYPE);
        sActivity.startActivity(intent);
        return this;
    }

    public AbsUpdateAPP setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public abstract int getProgressValue();

    /**
     * 获取apk文件存储路径
     * @return
     */
    private String getFileSavePath() {
        return FileHelper.getInstance().getFolders("apk");
    }

    @Override
    public boolean verifyVersion() {
        return false;
    }

    @Override
    public void showUpdateDialog() {
    }

    @Override
    public void downloadAPK() {

    }

    @Override
    public void installAPK(String file) {

    }

    @Override
    public void showNotifitation() {

    }

    protected static class WeakHandler extends Handler {

        WeakReference<Activity> weakReference;

        public WeakHandler(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = weakReference.get();
            if (activity == null) {
                return;
            }
        }
    }
}
