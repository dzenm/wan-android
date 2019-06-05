package com.dzenm.wanandroid.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.helper.log.Logger

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:36
 *
 * RecyclerView的一些常用设置的工具类
 */
class RecyclerViewHelper(val mRecyclerView: RecyclerView) {

    private val TAG = RecyclerViewHelper::class.java.simpleName

    /**
     * 滑动到最后一个Item的监听事件
     * @param onScrollLastItemListener
     * @return
     */
    fun addScrollToLastItemListener(onScrollLastItemListener: OnScrollLastItemListener): RecyclerViewHelper {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Logger.d(TAG + "---onScrollStateChanged")
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastPosition = layoutManager!!.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastPosition == itemCount - 1) {
                        Logger.d(TAG + "---scroll to page: " + lastPosition)
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
