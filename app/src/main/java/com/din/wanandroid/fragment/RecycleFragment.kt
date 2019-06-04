package com.din.wanandroid.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.din.wanandroid.R
import com.din.wanandroid.base.BaseFragment
import com.din.wanandroid.util.RecyclerViewHelper

open abstract class RecycleFragment : BaseFragment(), RecyclerViewHelper.OnScrollLastItemListener {

    protected var mPage: Int = 0                           // 加载的页数
    protected lateinit var swipe_refresh: SwipeRefreshLayout

    /**
     * 滑动刷新
     */
    abstract fun swipeRefreshing()

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
        swipe_refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        onAfterCreateView(rootView)
        // set Adapter
        recycler_view.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler_view.adapter = setAdapter()

        // initial data
        RecyclerViewHelper(recycler_view).addScrollToLastItemListener(this)
        swipe_refresh.setOnRefreshListener {
            mPage = 0
            swipeRefreshing()
        }
    }

    /**
     * 滑动到最后一个可见的item
     */
    override fun onLastItem(position: Int) {

    }
}