package com.dzenm.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dzenm.wanandroid.activities.WebActivity
import com.dzenm.wanandroid.adapter.WxArticleAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.api.CollectHelper
import com.dzenm.wanandroid.base.BaseAdapter
import com.dzenm.wanandroid.model.WxArticleModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author dinzhenyan
 * @date   2019-06-02 12:09
 */
class WxTabFragment(typeId: Int, isFirstPage: Boolean, mType: Int) :
    BaseTabItemFragment<WxArticleModel.Datas>(typeId, isFirstPage, mType) {

    companion object {
        fun newInstance(chapterId: Int, isFirstPage: Boolean, mType: Int): WxTabFragment {
            val wxTabItemFragment = WxTabFragment(chapterId, isFirstPage, mType)
            return wxTabItemFragment
        }
    }

    override fun onAfterCreateView(view: View) {
        mAdapter = WxArticleAdapter()
        (mAdapter as WxArticleAdapter).setItemOnClickListener(this)
        super.onAfterCreateView(view)
    }

    override fun getListData(lastPostion: Int, isLoadMore: Boolean) {
        ApiServicesHelper<WxArticleModel>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<WxArticleModel>() {
                override fun onNext(data: WxArticleModel) {
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
            }).request(Api.getService().getWxArticle(typeId, mPage))
    }

    /**
     * 收藏点击事件
     */
    override fun onItemCollectClick(bean: WxArticleModel.Datas, position: Int) {
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
    override fun onItemClick(bean: WxArticleModel.Datas, position: Int) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra(WebActivity.TITLE, bean.title)
        intent.putExtra(WebActivity.URL, bean.link)
        startActivity(intent)
    }
}