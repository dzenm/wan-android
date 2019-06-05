package com.dzenm.wanandroid.activities

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.helper.dialog.InfoDialog
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.adapter.CollectAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.api.CollectHelper
import com.dzenm.wanandroid.base.BaseActivity
import com.dzenm.wanandroid.base.BaseAdapter
import com.dzenm.wanandroid.model.CollectModel
import kotlinx.android.synthetic.main.activity_collect.*

class CollectActivity : BaseActivity(),
    CollectAdapter.OnItemClickListener {

    private var mAdapter: CollectAdapter =
        CollectAdapter()      // RecyclerView Adapter
    private var mPage: Int = 0                                   // 加载的页数
    private lateinit var mBeans: MutableList<CollectModel.Datas>

    override fun layoutId(): Int = R.layout.activity_collect

    override fun initialView() {
        setToolbar(toolbar)
        // set Adapter
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = mAdapter

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastItemPostion = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastItemPostion == (itemCount - 1)) {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
                        getCollectArticleData(lastItemPostion, true)
                    }
                }
            }
        })
        swipe_refresh.setOnRefreshListener {
            mPage = 0
            swipe_refresh.isRefreshing = false
            getCollectArticleData(0, false)
        }

        // RecyclerView Adapter initial
        mAdapter.setOnItemClickListener(this)

        getCollectArticleData(0, false)
    }

    fun getCollectArticleData(lastPostion: Int, isLoadMore: Boolean) {
        ApiServicesHelper<CollectModel>(this)
            .setOnCallback(object :
                com.dzenm.wanandroid.api.ApiServicesHelper.OnCallback<CollectModel>() {
                override fun onNext(data: CollectModel) {
                    mBeans = data.datas
                    val pageCount = data.pageCount
                    if (mPage <= pageCount) {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_COMPLETE)
                        mPage++
                    } else {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_END)
                    }
                    if (isLoadMore) {
                        mAdapter.addData(mBeans, lastPostion)
                    } else {
                        mAdapter.mBeans = mBeans
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }).request(Api.getService().getCollects(mPage))
    }

    /**
     * item点击事件
     */
    override fun onItemClick(bean: CollectModel.Datas, position: Int) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WebActivity.Companion.TITLE, bean.title)
        intent.putExtra(WebActivity.Companion.URL, bean.link)
        startActivity(intent)
    }

    override fun onItemLongClick(bean: CollectModel.Datas, position: Int) {
        InfoDialog.newInstance(this).setMessage("是否取消收藏？").setOnDialogClickListener { dialog, confirm ->
            if (confirm) {
                mAdapter.notifyItemRangeRemoved(position, 1)
                mBeans.removeAt(position)
                CollectHelper.uncollect(this, bean.id)
            }
            dialog.dismiss()
        }.show()
    }

}
