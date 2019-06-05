package com.dzenm.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dzenm.wanandroid.activities.WebActivity
import com.dzenm.wanandroid.adapter.ArticleAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.api.CollectHelper
import com.dzenm.wanandroid.base.BaseAdapter
import com.dzenm.wanandroid.model.ArticleModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author dinzhenyan
 * @date   2019-04-26 14:36
 */
class ProjectTabFragment(typeId: Int, isFirstPage: Boolean, mType: Int) :
    BaseTabItemFragment<ArticleModel.Datas>(typeId, isFirstPage, mType) {

    companion object {
        open fun newInstance(typeId: Int, isFirstPage: Boolean, mType: Int): ProjectTabFragment {
            val projectTabItemFragment = ProjectTabFragment(typeId, isFirstPage, mType)
            return projectTabItemFragment
        }
    }

    override fun onAfterCreateView(view: View) {
        mAdapter = ArticleAdapter()
        (mAdapter as ArticleAdapter).setItemOnClickListener(this)
        super.onAfterCreateView(view)
    }

    override fun getListData(lastPostion: Int, isLoadMore: Boolean) {
        ApiServicesHelper<ArticleModel>(activity as AppCompatActivity)
            .setOnCallback(object :
                ApiServicesHelper.OnCallback<ArticleModel>() {
                override fun onNext(data: ArticleModel) {
                    val datas = data.datas
                    val pageCount = data.pageCount
                    if (mPage <= pageCount) {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_COMPLETE)
                        mPage++
                    } else {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_END)
                    }
                    if (isLoadMore) {
                        mAdapter.addData(datas, lastPostion)
                    } else {
                        mAdapter.mBeans = datas
                        mAdapter.notifyDataSetChanged()

                        recycler_view.visibility = View.VISIBLE
                        swipe_refresh.isRefreshing = false
                    }
                }
            }).request(Api.getService().getProject(mPage, typeId))
    }

    /**
     * 收藏点击事件
     */
    override fun onItemCollectClick(bean: ArticleModel.Datas, position: Int) {
        if (bean.collect) {
            activity?.let { CollectHelper.uncollect(it, bean.id) }
            bean.collect = false
        } else {
            activity?.let { CollectHelper.collect(it, bean.id) }
            bean.collect = true
        }
    }

    /**
     * 点击进入查看
     */
    override fun onItemClick(bean: ArticleModel.Datas, position: Int) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra(WebActivity.TITLE, bean.title)
        intent.putExtra(WebActivity.URL, bean.link)
        startActivity(intent)
    }
}