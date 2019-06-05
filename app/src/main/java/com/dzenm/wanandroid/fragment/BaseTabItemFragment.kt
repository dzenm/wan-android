package com.dzenm.wanandroid.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.base.BaseAdapter

/**
 * @author dinzhenyan
 * @date   2019-06-02 22:08
 */
open class BaseTabItemFragment<T : Any>(val typeId: Int, val isFirstPage: Boolean, val mType: Int) : RecycleFragment(),
    BaseAdapter.OnItemClickListener<T> {

    protected lateinit var mAdapter: BaseAdapter<T>

    override fun layoutId(): Int = R.layout.fragment_tab_item

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = mAdapter

    override fun onAfterCreateView(view: View) {
        // RecyclerView Adapter initial
        swipe_refresh.isRefreshing = true
        if (isFirstPage) {
            getListData(1, false)
        }
    }

    override fun onLastItem(lastPosition: Int) {
        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
        getListData(lastPosition, true)
    }

    override fun swipeRefreshing() {
        getListData(1, false)
    }

    override fun lazyPrepareFetchData() {
        if (!isFirstPage) {
            getListData(1, false)
        }
    }

    open fun getListData(lastPostion: Int, isLoadMore: Boolean) {

    }

    /**
     * 收藏点击事件
     */
    override fun onItemCollectClick(bean: T, position: Int) {

    }

    /**
     * 点击进入查看
     */
    override fun onItemClick(bean: T, position: Int) {

    }
}