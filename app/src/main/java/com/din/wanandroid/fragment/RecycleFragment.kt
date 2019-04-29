package com.din.wanandroid.fragment

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.din.helper.dialog.PromptDialog
import com.din.wanandroid.R
import com.din.wanandroid.base.BaseFragment

open abstract class RecycleFragment : BaseFragment() {

    protected lateinit var promptDialog: PromptDialog     // 加载提示框
    protected var page: Int = 0                           // 加载的页数

    /**
     * 滑动到最后一个可见的item
     */
    abstract fun scrollToLastVisibleItem(lastPosition: Int)

    /**
     * 滑动刷新
     */
    abstract fun swipeToRefresh()

    abstract fun onAfterCreateView(view: View)

    /**
     * for recyclerview to set adapter
     */
    abstract fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

    /**
     * create view initial some view
     */
    override fun initiatedView() {
        val recycler_view = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        val swipe_refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        promptDialog = PromptDialog.newInstance(activity as Activity)

        onAfterCreateView(rootView)
        // set Adapter
        recycler_view.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler_view.adapter = setAdapter()

        // initial data
        onViewListener(recycler_view, swipe_refresh)
    }

    /**
     * 监听事件
     */
    fun onViewListener(recycler_view: RecyclerView, swipe_refresh: SwipeRefreshLayout) {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastItemPostion = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastItemPostion == (itemCount - 1)) {
                        scrollToLastVisibleItem(lastItemPostion)
                    }
                }
            }
        })
        swipe_refresh.setOnRefreshListener {
            page = 0
            swipe_refresh.isRefreshing = false
            swipeToRefresh()
        }
    }
}