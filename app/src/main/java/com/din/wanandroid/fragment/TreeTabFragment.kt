package com.din.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.din.wanandroid.activities.WebActivity
import com.din.wanandroid.adapter.TreeArticleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.ApiServicesHelper
import com.din.wanandroid.api.CollectHelper
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.TreeArticleModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author dinzhenyan
 * @date   2019-06-03 20:18
 */
class TreeTabFragment(typeId: Int, isFirstPage: Boolean, mType: Int) :
    BaseTabItemFragment<TreeArticleModel.Datas>(typeId, isFirstPage, mType) {

    companion object {
        fun newInstance(typeId: Int, isFirstPage: Boolean, mType: Int): TreeTabFragment {
            val treeTabFragment = TreeTabFragment(typeId, isFirstPage, mType)
            return treeTabFragment
        }
    }

    override fun onAfterCreateView(view: View) {
        mAdapter = TreeArticleAdapter()
        (mAdapter as TreeArticleAdapter).setItemOnClickListener(this)
        super.onAfterCreateView(view)
    }

    override fun getListData(lastPostion: Int, isLoadMore: Boolean) {
        ApiServicesHelper<TreeArticleModel>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<TreeArticleModel>() {
                override fun onNext(data: TreeArticleModel) {
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
                        swipe_refresh.isRefreshing = false
                        recycler_view.visibility = View.VISIBLE
                    }
                }
            }).request(Api.getService().getTreeArticle(mPage, typeId))
    }

    override fun onItemCollectClick(bean: TreeArticleModel.Datas, position: Int) {
        if (bean.collect) {
            activity?.let { CollectHelper.uncollect(it, bean.id) }
            bean.collect = false
        } else {
            activity?.let { CollectHelper.collect(it, bean.id) }
            bean.collect = true
        }
    }

    override fun onItemClick(bean: TreeArticleModel.Datas, position: Int) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra(WebActivity.TITLE, bean.title)
        intent.putExtra(WebActivity.URL, bean.link)
        startActivity(intent)
    }
}