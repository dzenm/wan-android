package com.din.wanandroid.view;

import android.content.Context;
import android.graphics.*;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.din.thedialog.util.Res;
import com.din.wanandroid.R;

/**
 * @author dinzhenyan
 * @date 2019/4/15 12:12 PM
 * @IDE Android Studio
 */
public class ImageTextView extends View {

    private static final int RING_WIDTH = Res.dp2px(20);
    private static final int RADIUS = Res.dp2px(150);
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HEIGHTLIGHT_COLOR = Color.parseColor("#FF4081");
    private Paint mPaint;
    private TextPaint mTextPaint;
    private StaticLayout staticLayout;
    private Bitmap mBitmap;
    private float[] cutWidth;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(Res.dp2px(14));
        mBitmap = getAvatar(Res.dp2px(100));

        // 文字换行
//        mTextPaint = new TextPaint();
//        mTextPaint.setTextSize(Res.dp2px(14));

        cutWidth = new float[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = "关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的" +
                "主要思想就是:我们并不关心这个鸭子是什么东西,我们只关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的主要" +
                "思想就是:我们并不关心这个鸭子是什么东西,我们只关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的主要思想" +
                "就是:我们并不关心这个鸭子是什么东西,我们只关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的" +
                "主要思想就是:我们并不关心这个鸭子是什么东西,我们只关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的主要" +
                "思想就是:我们并不关心这个鸭子是什么东西,我们只关于鸭子类型的定义不想赘述,很多书上包括文章上都有讲,他的主要思想" +
                "就是:我们并不关心这个鸭子是什么东西,我们只";
        // 文字换行
//        staticLayout = new StaticLayout(text, mTextPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL,
//                1, 0, false);
//        staticLayout.draw(canvas);

        canvas.drawBitmap(mBitmap, getWidth() - Res.dp2px(100), 100, mPaint);
        int oldIndex, index = 0;
        for (int i = 0; i < 6; i++) {
            oldIndex = index;
            index = mPaint.breakText(text, index, text.length(), true,
                    i < 2 ? getWidth() : getWidth() - Res.dp2px(100), cutWidth);
            canvas.drawText(text, oldIndex, oldIndex + index, 0, 50 + mPaint.getFontSpacing() * i, mPaint);
        }
    }

    /**
     * 加快图片读取的速度
     * @param width
     * @return
     */
    private Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.up_grade_ic_top, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.up_grade_ic_top, options);
    }
}
