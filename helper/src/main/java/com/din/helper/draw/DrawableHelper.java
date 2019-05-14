package com.din.helper.draw;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

/**
 * @author dinzhenyan
 * @date 2019-05-14 22:08
 * @IDE Android Studio
 */
public class DrawableHelper {

    public static GradientDrawable getDrawable(int radius) {
        float[] radii = new float[]{radius, radius, radius, radius};
        return getDrawable(Color.WHITE, radii);
    }

    public static GradientDrawable getDrawable(int color, int radius) {
        float[] radii = new float[]{radius, radius, radius, radius};
        return getDrawable(color, radii);
    }

    public static GradientDrawable getDrawable(float[] radii) {
        return getDrawable(Color.WHITE, radii, 0, Color.TRANSPARENT);
    }

    public static GradientDrawable getDrawable(int color, float[] radii) {
        return getDrawable(color, radii, 0, Color.TRANSPARENT);
    }

    public static GradientDrawable getDrawable(int radius, int stroke, int strokeColor) {
        return getDrawable(Color.WHITE, radius, stroke, strokeColor);
    }

    public static GradientDrawable getDrawable(float[] radii, int stroke, int strokeColor) {
        return getDrawable(Color.WHITE, radii, stroke, strokeColor);
    }

    public static GradientDrawable getDrawable(int color, int radius, int stroke, int strokeColor) {
        float[] radii = new float[]{radius, radius, radius, radius};
        return getDrawable(color, radii, dp2px(stroke), strokeColor);
    }

    public static GradientDrawable getDrawable(int color, float[] radii, int stroke, int strokeColor) {
        float[] radi = new float[]{dp2px(radii[0]), dp2px(radii[0]), dp2px(radii[1]), dp2px(radii[1]),
                dp2px(radii[2]), dp2px(radii[2]), dp2px(radii[3]), dp2px(radii[3])};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadii(radi);
        drawable.setStroke(dp2px(stroke), strokeColor);
        drawable.setUseLevel(true);
        return drawable;
    }

    public static int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}
