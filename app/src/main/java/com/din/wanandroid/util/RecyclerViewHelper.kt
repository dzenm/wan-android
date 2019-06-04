package com.din.wanandroid.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:36
 *
 * RecyclerView的一些常用设置的工具类
 */
class RecyclerViewHelper(val mRecyclerView: RecyclerView) {

    /**
     * 滑动到最后一个Item的监听事件
     * @param onScrollLastItemListener
     * @return
     */
    fun addScrollToLastItemListener(onScrollLastItemListener: OnScrollLastItemListener): RecyclerViewHelper {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastPosition = layoutManager!!.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastPosition == itemCount - 1) {
                        onScrollLastItemListener.onLastItem(lastPosition)
                    }
                }
            }
        })
        return this
    }

    interface OnScrollLastItemListener {

        fun onLastItem(lastPosition: Int)

    }
}
