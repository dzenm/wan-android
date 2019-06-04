package com.din.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.activities.TabActivity
import com.din.wanandroid.activities.TabActivity.Companion.BEAN_POSITION
import com.din.wanandroid.activities.TabActivity.Companion.BEAN_TYPE
import com.din.wanandroid.activities.WebActivity
import com.din.wanandroid.adapter.MultipleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.ApiServicesHelper
import com.din.wanandroid.model.MultipleTitleBean
import com.din.wanandroid.model.NewProjectModel
import com.din.wanandroid.model.WxModel

class ProjectFragment : RecycleFragment(), MultipleAdapter.OnItemClickListener {

    private var mAdapter: MultipleAdapter = MultipleAdapter()        // RecyclerView Adapter

    private var mBeans: MutableList<Any> = mutableListOf()

    override fun layoutId(): Int = R.layout.fragment_project

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = mAdapter

    override fun swipeRefreshing() {
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
        ApiServicesHelper<NewProjectModel>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<NewProjectModel>() {
                override fun onNext(data: NewProjectModel) {
                    mBeans.clear()
                    mBeans.add(MultipleTitleBean("项目", MultipleAdapter.TYPE_PROJECT))
                    mBeans.addAll(data.datas)
                    getWxListData()
                }
            }).request(Api.getService().getNewProject(mPage))
    }

    /**
     * 获取微信公众号的数据
     */
    fun getWxListData() {
        ApiServicesHelper<MutableList<WxModel>>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<WxModel>>() {
                override fun onNext(data: MutableList<WxModel>) {
                    mBeans.add(MultipleTitleBean("公众号", MultipleAdapter.TYPE_WX))
                    mBeans.addAll(data)
                    mAdapter.addData(mBeans)
                    swipe_refresh.isRefreshing = false
                }
            }).request(Api.getService().getWx())
    }

    override fun onItemClick(position: Int) {
        if (mBeans.get(position) is MultipleTitleBean) {
            // 点击标题中的查看更多
            val bean = mBeans.get(position) as MultipleTitleBean
            val intent = Intent(activity, TabActivity::class.java)
            intent.putExtra(BEAN_TYPE, bean.type)
            startActivity(intent)
        } else if (mBeans.get(position) is NewProjectModel.Datas) {
            // 跳转至项目分类查看
            val bean = mBeans.get(position) as NewProjectModel.Datas
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(WebActivity.TITLE, bean.title)
            intent.putExtra(WebActivity.URL, bean.projectLink)
            startActivity(intent)
        } else if (mBeans.get(position) is WxModel) {
            // 跳转至微信公众号分类查看，选中的Tab
            val bean = mBeans.get(position) as WxModel
            val intent = Intent(activity, TabActivity::class.java)
            intent.putExtra(BEAN_TYPE, MultipleAdapter.TYPE_WX)
            intent.putExtra(BEAN_POSITION, bean.id)
            startActivity(intent)
        }
    }
}
