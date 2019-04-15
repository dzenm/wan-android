package com.din.thedialog.pig;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 啥是佩奇
 */
public class PageView extends View implements ValueAnimator.AnimatorUpdateListener {

    private RectF rect;
    private Paint paintPink;
    private Paint paintRed = new Paint();
    private Paint paintBlack = new Paint();

    private Path mPath = new Path();
    private Path mPathEarLeft = new Path();
    private Path mPathEarRight = new Path();
    private Path mPathBody = new Path();
    private Path mPathArmRight = new Path();
    private Path mPathHandRight = new Path();
    private Path mPathArmLeft = new Path();
    private Path mPathHandLeft = new Path();
    private Path mPathTail = new Path();

    // 常规百分百绘制
    private ValueAnimator animNose;
    private ValueAnimator animEyes;
    private ValueAnimator animFace;
    private ValueAnimator animMouth;
    private ValueAnimator animLegs;
    private ValueAnimator animFoots;
    private int progressNose = 0;
    private int progressEyes = 0;
    private int progressFace = 0;
    private int progressMouth = 0;
    private int progressLegs = 0;
    private int progressFoots = 0;

    // 贝塞尔曲线绘制
    private ValueAnimator animHead;
    private ValueAnimator animEarLeft;
    private ValueAnimator animEarRight;
    private ValueAnimator animBody;
    private ValueAnimator animArmRight;
    private ValueAnimator animHandRight;
    private ValueAnimator animArmLeft;
    private ValueAnimator animHandLeft;
    private ValueAnimator animTail;

    private ViewPoint pointHead = new ViewPoint();
    private ViewPoint pointEarLeft = new ViewPoint();
    private ViewPoint pointEarRight = new ViewPoint();
    private ViewPoint pointBody = new ViewPoint();
    private ViewPoint pointArmRight = new ViewPoint();
    private ViewPoint pointHandRight = new ViewPoint();
    private ViewPoint pointArmLeft = new ViewPoint();
    private ViewPoint pointHandLeft = new ViewPoint();
    private ViewPoint pointTail = new ViewPoint();

    boolean isInitPath = false;
    private AnimatorSet animatorSet;

    public PageView(Context context) {
        super(context);
        init();
    }

    public PageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        // 初始化矩形，各个部位的父容器，如鼻子是在矩形内部画椭圆
        rect = new RectF();
        paintPink = newPaint(Color.rgb(255, 155, 192));
        paintRed = newPaint(Color.rgb(255, 99, 71));
        paintBlack = newPaint(Color.BLACK);
    }

    private Paint newPaint(int color) {
        Paint mPaint = new Paint();             // 创建画笔
        mPaint.setColor(color);                 // 设置画笔的颜色
        mPaint.setStyle(Paint.Style.STROKE);    // 设置画笔的填充方式：描边
        mPaint.setStrokeWidth(3f);              // 设置画笔的宽度
        mPaint.setAntiAlias(true);              // 设置抗锯齿，可以圆润一些
        return mPaint;
    }

    /**
     * 普通动画，获取百分百
     */
    private void initIntAnim() {
        animNose = newAnimator(1000);      // 鼻子
        animEyes = newAnimator(800);       // 眼睛
        animFace = newAnimator(800);       // 腮红
        animMouth = newAnimator(500);      // 嘴巴
        animLegs = newAnimator(400);       // 腿
        animFoots = newAnimator(400);      // 脚
    }

    private ValueAnimator newAnimator(long duration) {
        ValueAnimator mValueAnimator = new ValueAnimator();
        mValueAnimator = ValueAnimator.ofInt(0, 100);   // 设置动画的起始值，也就是我们需要的进度变化区间
        mValueAnimator.addUpdateListener(this);         // 监听动画进度变化，并执行重绘操作
        mValueAnimator.setDuration(duration);           // 设置动画时长
        return mValueAnimator;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == animNose) {
            progressNose = (int) animation.getAnimatedValue();
        } else if (animation == animEyes) {
            progressEyes = (int) animation.getAnimatedValue();
        } else if (animation == animFace) {
            progressFace = (int) animation.getAnimatedValue();
        } else if (animation == animMouth) {
            progressMouth = (int) animation.getAnimatedValue();
        } else if (animation == animLegs) {
            progressLegs = (int) animation.getAnimatedValue();
        } else if (animation == animFoots) {
            progressFoots = (int) animation.getAnimatedValue();
        } else if (animation == animHead) {
            pointHead = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animEarLeft) {
            pointEarLeft = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animEarRight) {
            pointEarRight = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animBody) {
            pointBody = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animHandRight) {
            pointHandRight = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animHandLeft) {
            pointHandLeft = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animArmRight) {
            pointArmRight = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animArmLeft) {
            pointArmLeft = (ViewPoint) animation.getAnimatedValue();
        } else if (animation == animTail) {
            pointTail = (ViewPoint) animation.getAnimatedValue();
        }
        invalidate();
    }

    public void initPath() {    // 我们的ViewPath，其实可以绘制任何直线路径和贝塞尔曲线路径了，自己在调用lineTo传入点等就行了
        // 猪头 🐷
        ViewPath viewPath = new ViewPath();
        pointHead.x = dp2px(220);
        pointHead.y = dp2px(102);
        mPath.moveTo(pointHead.x, pointHead.y);
        viewPath.moveTo(pointHead.x, pointHead.y);
        viewPath.curveTo(dp2px(-100), dp2px(80), dp2px(130), dp2px(330), dp2px(170), dp2px(170));
        viewPath.quadTo(dp2px(210), dp2px(170), dp2px(240), dp2px(155));
        animHead = newValueAnimator(viewPath, 3000);

        // 右耳朵
        ViewPath viewPathEarLeft = new ViewPath();
        pointEarLeft.x = dp2px(130);
        pointEarLeft.y = dp2px(105);
        mPathEarLeft.moveTo(pointEarLeft.x, pointEarLeft.y);
        viewPathEarLeft.moveTo(pointEarLeft.x, pointEarLeft.y);
        viewPathEarLeft.curveTo(dp2px(120), dp2px(50), dp2px(160), dp2px(50), dp2px(150), dp2px(102));
        animEarLeft = newValueAnimator(viewPathEarLeft, 600);

        // 左耳朵
        ViewPath viewPathEarRight = new ViewPath();
        pointEarRight.x = dp2px(100);
        pointEarRight.y = dp2px(110);
        mPathEarRight.moveTo(pointEarRight.x, pointEarRight.y);
        viewPathEarRight.moveTo(pointEarRight.x, pointEarRight.y);
        viewPathEarRight.curveTo(dp2px(80), dp2px(53), dp2px(120), dp2px(53), dp2px(120), dp2px(105));
        animEarRight = newValueAnimator(viewPathEarRight, 600);

        // 身体：肚子
        ViewPath viewPathBody = new ViewPath();
        pointBody.x = dp2px(80);
        pointBody.y = dp2px(210);
        mPathBody.moveTo(pointBody.x, pointBody.y);
        viewPathBody.moveTo(pointBody.x, pointBody.y);
        viewPathBody.quadTo(dp2px(50), dp2px(270), dp2px(50), dp2px(320));
        viewPathBody.quadTo(dp2px(100), dp2px(320), dp2px(180), dp2px(320));
        viewPathBody.quadTo(dp2px(180), dp2px(270), dp2px(150), dp2px(210));
        animBody = newValueAnimator(viewPathBody, 2000);

        // 左手
        pointHandLeft.x = dp2px(20);
        pointHandLeft.y = dp2px(235);
        animHandLeft = newValueAnimator(handPath(mPathHandLeft, pointHandLeft.x, pointHandLeft.y, 40, 242, 22, 255), 500);

        // 右手
        pointHandRight.x = dp2px(210);
        pointHandRight.y = dp2px(235);
        animHandRight = newValueAnimator(handPath(mPathHandRight, pointHandRight.x, pointHandRight.y, 190, 242, 207, 255), 500);

        // 左胳膊
        pointArmLeft.x = dp2px(70);
        pointArmLeft.y = dp2px(233);
        animArmLeft = newValueAnimator(handPath(mPathArmLeft, pointArmLeft.x, pointArmLeft.y, 70, 230, 20, 245), 500);

        // 右胳膊
        pointArmRight.x = dp2px(160);
        pointArmRight.y = dp2px(233);
        animArmRight = newValueAnimator(handPath(mPathArmRight, pointArmRight.x, pointArmRight.y, 170, 230, 210, 245), 500);

        // 尾巴
        ViewPath viewPathTail = new ViewPath();
        pointTail.x = dp2px(51);
        pointTail.y = dp2px(300);
        mPathTail.moveTo(pointTail.x, pointTail.y);
        viewPathTail.moveTo(pointTail.x, pointTail.y);
        viewPathTail.curveTo(dp2px(30), dp2px(330), dp2px(30), dp2px(280), dp2px(40), dp2px(300));
        viewPathTail.curveTo(dp2px(40), dp2px(320), dp2px(20), dp2px(300), dp2px(0), dp2px(310));
        animTail = newValueAnimator(viewPathTail, 1200);

        animatorSet = new AnimatorSet();
        // 设置动画集合，按顺序绘制
        animatorSet.playSequentially(animNose, animHead, animEarRight, animEarLeft, animEyes, animMouth, animFace,
                animBody, animArmRight, animHandRight, animArmLeft, animHandLeft, animLegs, animFoots, animTail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (widthMeasureSpec > 0) {
            if (!isInitPath) {
                isInitPath = true;
                initIntAnim();
                initPath();
            }
        }
    }

    private ViewPath handPath(Path path, float x, float y, float x1, float y1, float x2, float y2) {
        ViewPath viewPath = new ViewPath();
        path.moveTo(x, y);
        viewPath.moveTo(x, y);
        viewPath.quadTo(dp2px(x1), dp2px(y1), dp2px(x2), dp2px(y2));
        return viewPath;
    }

    private ValueAnimator newValueAnimator(ViewPath viewPath, long duration) {
        ValueAnimator animator = new ValueAnimator();
        animator = ValueAnimator.ofObject(new ViewPathEvaluator(), viewPath.getPoints().toArray());
        animator.addUpdateListener(this);
        animator.setDuration(duration);
        return animator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 鼻子：倾斜的椭圆
        rect.set(dp2px(200), dp2px(101), dp2px(250), dp2px(160));
        // 旋转画布，结束还需旋转回去（在这里实现倾斜）
        canvas.rotate(-15, dp2px(225), dp2px(150));
        if (progressNose < 100) {
            // 如果进度不完整，只进行描边操作
            paintPink.setStyle(Paint.Style.STROKE);
            paintRed.setStyle(Paint.Style.STROKE);
        } else {
            // 如果进度完整，即环形绘制完成，设置画笔为填充模式,设置填充及描边（FILL_AND_STROKE）也行
            paintPink.setStyle(Paint.Style.FILL);
            paintRed.setStyle(Paint.Style.FILL);
        }
        // 画扇形：如果角度为360度，就是矩形的内切椭圆，如果矩形为正方形，则椭圆为正圆
        canvas.drawArc(rect, 0, progressNose * 3.6f, false, paintPink);
        canvas.rotate(15, dp2px(225), dp2px(130));

        // 鼻孔
        // 重新设置矩形的参数为正方形
        rect.set(dp2px(213), dp2px(125), dp2px(223), dp2px(135));
        // 根据进度画圆形鼻孔
        canvas.drawArc(rect, 0, progressNose * 3.6f, false, paintRed);
        rect.set(dp2px(230), dp2px(122), dp2px(240), dp2px(132));
        canvas.drawArc(rect, 0, progressNose * 3.6f, false, paintRed);

        // 重新设置画笔为描边
        paintPink.setStyle(Paint.Style.STROKE);
        paintRed.setStyle(Paint.Style.STROKE);

        // 眼睛部分：眼眶
        rect.set(dp2px(110), dp2px(115), dp2px(140), dp2px(145));
        canvas.drawArc(rect, 0, progressEyes * 3.6f, false, paintPink);
        rect.set(dp2px(145), dp2px(105), dp2px(175), dp2px(135));
        canvas.drawArc(rect, 0, progressEyes * 3.6f, false, paintPink);
        // 黑眼球
        if (progressEyes < 100) {
            paintBlack.setStyle(Paint.Style.STROKE);
        } else {
            paintBlack.setStyle(Paint.Style.FILL);
        }
        rect.set(dp2px(123), dp2px(123), dp2px(133), dp2px(133));
        canvas.drawArc(rect, 0, progressEyes * 3.6f, false, paintBlack);
        rect.set(dp2px(158), dp2px(113), dp2px(168), dp2px(123));
        canvas.drawArc(rect, 0, progressEyes * 3.6f, false, paintBlack);

        // 腮红
        if (progressFace < 100) {
            paintPink.setStyle(Paint.Style.STROKE);
        } else {
            paintPink.setStyle(Paint.Style.FILL);
        }
        rect.set(dp2px(70), dp2px(160), dp2px(95), dp2px(190));
        canvas.drawArc(rect, 0, progressFace * 3.6f, false, paintPink);

        // 嘴巴
        rect.set(dp2px(110), dp2px(175), dp2px(155), dp2px(200));
        canvas.drawArc(rect, 165, -progressMouth * 1.8f, false, paintRed);

        // 腿和脚，需要内容填充（眼睛绘制完成，黑色画笔已是填充状态）
        paintPink.setStyle(Paint.Style.FILL);

        // 腿
        canvas.drawRect(dp2px(95), dp2px(320), dp2px(98), dp2px(320 + 30 * progressLegs / 100f), paintPink);
        canvas.drawRect(dp2px(130), dp2px(320), dp2px(133), dp2px(320 + 30 * progressLegs / 100f), paintPink);

        // 小黑脚
        rect.set(dp2px(90), dp2px(350), dp2px(90 + 20 * progressFoots / 100f), dp2px(360));
        canvas.drawRoundRect(rect, dp2px(5), dp2px(5), paintBlack);
        rect.set(dp2px(125), dp2px(350), dp2px(125 + 20 * progressFoots / 100f), dp2px(360));
        canvas.drawRoundRect(rect, dp2px(5), dp2px(5), paintBlack);

        paintPink.setStyle(Paint.Style.STROKE);
        paintBlack.setStyle(Paint.Style.STROKE);

        // 身体轮廓：贝塞尔曲线
        // 头部
        mPath.lineTo(pointHead.x, pointHead.y);
        canvas.drawPath(mPath, paintPink);
        // 右耳朵
        mPathEarLeft.lineTo(pointEarLeft.x, pointEarLeft.y);
        canvas.drawPath(mPathEarLeft, paintPink);
        // 左耳朵
        mPathEarRight.lineTo(pointEarRight.x, pointEarRight.y);
        canvas.drawPath(mPathEarRight, paintPink);
        // 肚子
        mPathBody.lineTo(pointBody.x, pointBody.y);
        canvas.drawPath(mPathBody, paintRed);
        // 右胳膊
        mPathArmRight.lineTo(pointArmRight.x, pointArmRight.y);
        canvas.drawPath(mPathArmRight, paintPink);
        // 右手
        mPathHandRight.lineTo(pointHandRight.x, pointHandRight.y);
        canvas.drawPath(mPathHandRight, paintPink);
        // 左胳膊
        mPathArmLeft.lineTo(pointArmLeft.x, pointArmLeft.y);
        canvas.drawPath(mPathArmLeft, paintPink);
        // 左手
        mPathHandLeft.lineTo(pointHandLeft.x, pointHandLeft.y);
        canvas.drawPath(mPathHandLeft, paintPink);
        // 尾巴
        mPathTail.lineTo(pointTail.x, pointTail.y);
        canvas.drawPath(mPathTail, paintPink);
    }

    private int dp2px(float dpValue) {
        return (int) (dpValue * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public void startAnimation() {
        // 再次绘制需要置空，并重新设置Path起点，如果只绘制一次，无需此操作
        reset();
        initPath();
        animatorSet.start();
    }

    private void reset() {
        mPath.reset();
        mPathEarLeft.reset();
        mPathEarRight.reset();
        mPathBody.reset();
        mPathArmRight.reset();
        mPathHandRight.reset();
        mPathArmLeft.reset();
        mPathHandLeft.reset();
        mPathTail.reset();
        animatorSet.cancel();
    }
}