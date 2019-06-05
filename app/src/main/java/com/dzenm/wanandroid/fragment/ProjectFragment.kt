package com.dzenm.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.activities.TabActivity
import com.dzenm.wanandroid.activities.TabActivity.Companion.BEAN_POSITION
import com.dzenm.wanandroid.activities.TabActivity.Companion.BEAN_TYPE
import com.dzenm.wanandroid.activities.WebActivity
import com.dzenm.wanandroid.adapter.MultipleAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.model.MultipleTitleBean
import com.dzenm.wanandroid.model.NewProjectModel
import com.dzenm.wanandroid.model.WxModel
import com.dzenm.helper.log.Logger

class ProjectFragment : com.dzenm.wanandroid.fragment.RecycleFragment(), com.dzenm.wanandroid.adapter.MultipleAdapter.OnItemClickListener {

    private var mAdapter: com.dzenm.wanandroid.adapter.MultipleAdapter =
        com.dzenm.wanandroid.adapter.MultipleAdapter()        // RecyclerView Adapter

    private var mBeans: MutableList<Any> = mutableListOf()

    override fun layoutId(): Int = R.layout.fragment_hall_item

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = mAdapter

    override fun swipeRefreshing() {
        Logger.d(TAG + "---refreshing")
        // 清楚数据，重新请求
        mPage = 0
        getNewPrejectListData()
    }

    override fun onAfterCreateView(view: View) {
        mAdapter.setOnItemClickListener(this)
        swipe_refresh.isRefreshing = true
        getNewPrejectListData()
    }

    /**
     * 获取最新项目的数据
     */
    fun getNewPrejectListData() {
        com.dzenm.wanandroid.api.ApiServicesHelper<com.dzenm.wanandroid.model.NewProjectModel>(activity as AppCompatActivity)
            .setOnCallback(object : com.dzenm.wanandroid.api.ApiServicesHelper.OnCallback<com.dzenm.wanandroid.model.NewProjectModel>() {
                override fun onNext(data: com.dzenm.wanandroid.model.NewProjectModel) {
                    mBeans.clear()
                    mBeans.add(
                        com.dzenm.wanandroid.model.MultipleTitleBean(
                            "项目",
                            com.dzenm.wanandroid.adapter.MultipleAdapter.TYPE_PROJECT
                        )
                    )
                    mBeans.addAll(data.datas)
                    Logger.d(TAG + "---获取最新项目的数据: " + data)
                    getWxListData()
                }
            }).request(com.dzenm.wanandroid.api.Api.getService().getNewProject(mPage))
    }

    /**
     * 获取微信公众号的数据
     */
    fun getWxListData() {
        com.dzenm.wanandroid.api.ApiServicesHelper<MutableList<com.dzenm.wanandroid.model.WxModel>>(activity as AppCompatActivity)
            .setOnCallback(object : com.dzenm.wanandroid.api.ApiServicesHelper.OnCallback<MutableList<com.dzenm.wanandroid.model.WxModel>>() {
                override fun onNext(data: MutableList<com.dzenm.wanandroid.model.WxModel>) {
                    mBeans.add(
                        com.dzenm.wanandroid.model.MultipleTitleBean(
                            "公众号",
                            com.dzenm.wanandroid.adapter.MultipleAdapter.TYPE_WX
                        )
                    )
                    mBeans.addAll(data)
                    mAdapter.addData(mBeans)
                    Logger.d(TAG + "---获取微信公众号的数据: " + data)
                    swipe_refresh.isRefreshing = false
                }
            }).request(com.dzenm.wanandroid.api.Api.getService().getWx())
    }

    override fun onItemClick(position: Int) {
        if (mBeans.get(position) is com.dzenm.wanandroid.model.MultipleTitleBean) {
            // 点击标题中的查看更多
            val bean = mBeans.get(position) as com.dzenm.wanandroid.model.MultipleTitleBean
            val intent = Intent(activity, com.dzenm.wanandroid.activities.TabActivity::class.java)
            intent.putExtra(BEAN_TYPE, bean.type)
            startActivity(intent)
        } else if (mBeans.get(position) is com.dzenm.wanandroid.model.NewProjectModel.Datas) {
            // 跳转至项目分类查看
            val bean = mBeans.get(position) as com.dzenm.wanandroid.model.NewProjectModel.Datas
            val intent = Intent(activity, com.dzenm.wanandroid.activities.WebActivity::class.java)
            intent.putExtra(com.dzenm.wanandroid.activities.WebActivity.TITLE, bean.title)
            intent.putExtra(com.dzenm.wanandroid.activities.WebActivity.URL, bean.projectLink)
            startActivity(intent)
        } else if (mBeans.get(position) is com.dzenm.wanandroid.model.WxModel) {
            // 跳转至微信公众号分类查看，选中的Tab
            val bean = mBeans.get(position) as com.dzenm.wanandroid.model.WxModel
            val intent = Intent(activity, com.dzenm.wanandroid.activities.TabActivity::class.java)
            intent.putExtra(BEAN_TYPE, com.dzenm.wanandroid.adapter.MultipleAdapter.TYPE_WX)
            intent.putExtra(BEAN_POSITION, bean.id)
            startActivity(intent)
        }
    }
}
