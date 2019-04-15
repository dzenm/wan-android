package com.din.wanandroid.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.din.wanandroid.R;

/**
 * @author dinzhenyan
 * @date 2019/4/16 12:14 AM
 * @IDE Android Studio
 */
public class CameraView extends View {

    private Paint mPaint;
    private Camera mCamera;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCamera = new Camera();
        mCamera.rotateX(30);
        mCamera.setLocation(0, 0, getZForCamera());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(100 + 400 / 2, 100 + 400 / 2);
        mCamera.applyToCanvas(canvas);
        canvas.clipRect(-(600 / 2), 0, 600 / 2, 600 / 2);
        canvas.translate(-(100 + 400 / 2), -(100 + 400 / 2));
        canvas.drawBitmap(getAvatar(400), 100, 100, mPaint);
    }

    /**
     * 加快图片读取的速度
     * @param width
     * @return
     */
    public Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.up_grade_ic_top, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.up_grade_ic_top, options);
    }

    private float getZForCamera() {
        return -6 * Resources.getSystem().getDisplayMetrics().density;
    }
}
