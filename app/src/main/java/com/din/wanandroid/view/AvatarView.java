package com.din.wanandroid.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.din.thedialog.Res;
import com.din.wanandroid.R;

/**
 * @author dinzhenyan
 * @date 2019/4/14 11:43 PM
 * @IDE Android Studio
 */
public class AvatarView extends View {

    private static final int WIDTH = Res.dp2px(200);
    private static final int PADDING = Res.dp2px(50);

    private Bitmap mBitmap;
    private Paint mPaint;
    private Xfermode mXfermode;
    private RectF saveArea;

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mBitmap = getAvatar(WIDTH);
        mPaint = new Paint();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        saveArea = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        saveArea.set(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saved = canvas.saveLayer(saveArea, mPaint);
        canvas.drawOval(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH, mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mBitmap, PADDING, PADDING, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saved);
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
}
