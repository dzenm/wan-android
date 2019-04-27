package com.din.banner;

/**
 * @author dinzhenyan
 * @date 2019-04-21 18:35
 * @IDE Android Studio
 */
public class Point {

    /**
     * 每次滑动之后对小圆点重新调整
     *
     * @param position
     * @param offset
     */
    public float movePoint(int position, float offset, int mCurrentPosition, int size) {
        float offsetDistance = 0;
        if (position == 0) {                    // 左滑(offset从1.0-0.0结束)
            if (mCurrentPosition == 0) {         // 是否是第一个向左滑动，并且显示到最后一个
                if (offset > 0.5) {
                    offsetDistance = offset - 1;
                } else {
                    offsetDistance = (size - 1) + offset;
                }
            } else {
                offsetDistance = mCurrentPosition - (1 - offset);
            }
        } else if (position == 1) {             // 右滑(offset从0.0-1.0结束)
            if (mCurrentPosition == size - 1) {  // 是否是最后一个向左滑动，并且显示到第一个
                if (offset < 0.5) {
                    offsetDistance = (size - 1) + offset;
                } else {
                    offsetDistance = offset - 1;
                }
            } else {
                offsetDistance = mCurrentPosition + offset;
            }
        }
        return offsetDistance;
    }
}
