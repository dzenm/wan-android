package com.din.wanandroid.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.din.wanandroid.R
import com.din.wanandroid.activities.TabActivity
import com.din.wanandroid.adapter.MultipleAdapter
import com.din.wanandroid.adapter.TreeAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.ApiServicesHelper
import com.din.wanandroid.base.BaseFragment
import com.din.wanandroid.model.MultipleTitleBean
import com.din.wanandroid.model.TreeModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TreeFragment : BaseFragment(), TreeAdapter.OnItemClickListener {

    private val mAdapter = TreeAdapter()

    private var mBeans: MutableList<Any> = mutableListOf()

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun layoutId(): Int = R.layout.fragment_tree

    override fun initiatedView() {
        super.initiatedView()

        val recycler_view = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        swipe_refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        // set Adapter
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START

        // refresh data
        swipe_refresh.setOnRefreshListener {
            getTreeListData()
        }

        // set adapter andr recyclerview
        mAdapter.setOnItemClickListener(this)
        recycler_view.layoutManager = flexboxLayoutManager
        recycler_view.adapter = mAdapter
    }

    // fetch data
    override fun lazyPrepareFetchData() {
        swipe_refresh.isRefreshing = true
        getTreeListData()
    }

    fun getTreeListData() {
        ApiServicesHelper<MutableList<TreeModel>>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<TreeModel>>() {
                override fun onNext(data: MutableList<TreeModel>) {
                    mBeans.clear()
                    for (i in data.indices) {
                        val treeModel = data.get(i)
                        mBeans.add(MultipleTitleBean(treeModel.name, MultipleAdapter.TYPE_WX))
                        mBeans.addAll(treeModel.children)
                    }
                    mAdapter.addData(mBeans)
                    swipe_refresh.isRefreshing = false
                }
            }).request(Api.getService().getTree())
    }

    override fun onItemClick(position: Int) {
        val bean = mBeans.get(position) as TreeModel.Children
        val intent = Intent(activity, TabActivity::class.java)
        intent.putExtra(TabActivity.BEAN_TYPE, MultipleAdapter.TYPE_TREE)
        intent.putExtra(TabActivity.BEAN_POSITION, bean.id)
        startActivity(intent)
    }
}
