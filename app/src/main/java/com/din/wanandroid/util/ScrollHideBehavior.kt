package com.din.wanandroid.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

class ScrollHideBehavior : CoordinatorLayout.Behavior<View> {

    private var delayY: Float = 0F      // 列表顶部和title底部重合时，列表的滑动距离。

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor() : super()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is RecyclerView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (delayY == 0F) {
            delayY = dependency.y - child.height
        }
        var dy = dependency.y - child.height
        if (dy < 0) {
            dy = 0F
        } else {
            dy = dy
        }
        val alpha = 1 - (dy / delayY)
        child.alpha = alpha
        return true
    }
}