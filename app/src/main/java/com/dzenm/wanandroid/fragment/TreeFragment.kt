package com.dzenm.wanandroid.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dzenm.helper.log.Logger
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.activities.TabActivity
import com.dzenm.wanandroid.adapter.MultipleAdapter
import com.dzenm.wanandroid.adapter.TreeAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.base.BaseFragment
import com.dzenm.wanandroid.model.MultipleTitleBean
import com.dzenm.wanandroid.model.TreeModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TreeFragment : BaseFragment(), TreeAdapter.OnItemClickListener {

    private val mAdapter = TreeAdapter()

    private var mBeans: MutableList<Any> = mutableListOf()

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun layoutId(): Int = R.layout.fragment_hall_item

    override fun initiatedView() {
        super.initiatedView()

        val recycler_view = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        swipe_refresh = rootView.findViewById(R.id.swipe_refresh)

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
        Logger.d(TAG + "---initiatedView")
    }

    // fetch data
    override fun lazyPrepareFetchData() {
        swipe_refresh.isRefreshing = true
        getTreeListData()
    }

    /**
     * 获取体系文章的数据
     */
    fun getTreeListData() {
        ApiServicesHelper<MutableList<TreeModel>>(activity as AppCompatActivity)
            .setOnCallback(object :
                ApiServicesHelper.OnCallback<MutableList<TreeModel>>() {
                override fun onNext(data: MutableList<TreeModel>) {
                    mBeans.clear()
                    for (i in data.indices) {
                        val treeModel = data.get(i)
                        mBeans.add(
                            MultipleTitleBean(
                                treeModel.name,
                                MultipleAdapter.TYPE_WX
                            )
                        )
                        mBeans.addAll(treeModel.children)
                    }
                    mAdapter.addData(mBeans)
                    Logger.d(TAG + "---获取体系文章的数据: " + data)
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
