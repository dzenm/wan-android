package com.din.helper.util;

import android.os.Handler;
import android.widget.Toast;
import com.din.helper.dialog.LoadProgress;
import com.din.helper.dialog.UpGradeDialog;

/**
 * @author dinzhenyan
 * @date 2019-04-29 20:36
 * @IDE Android Studio
 */
public class UpdateAPP extends AbsUpdateAPP implements UpGradeDialog.OnUpGradeListener {

    private Handler handler = new WeakHandler(sActivity);

    @Override
    public boolean verifyVersion() {
        return false;
    }

    @Override
    public int getProgressValue() {

        return 0;
    }

    @Override
    public void showUpdateDialog() {
        if (verifyVersion()) {
            UpGradeDialog.newInstance(sActivity).setOnUpGradeListener(this).build();
        }
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    public void onStart(LoadProgress loadProgress) {
        getProgressValue();
    }

    @Override
    public void onFinished() {
        Toast.makeText(sActivity, "升级完成，进行安装", Toast.LENGTH_SHORT).show();
    }
}
