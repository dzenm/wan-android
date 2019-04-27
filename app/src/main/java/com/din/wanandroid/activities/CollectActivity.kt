package com.din.wanandroid.activities

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.din.thedialog.InfoDialog
import com.din.thedialog.PromptDialog
import com.din.wanandroid.R
import com.din.wanandroid.adapter.CollectAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.CollectApi
import com.din.wanandroid.api.CollectHelper
import com.din.wanandroid.base.BaseActivity
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.CollectModel
import kotlinx.android.synthetic.main.activity_collect.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CollectActivity : BaseActivity(), CollectAdapter.OnItemClickListener {

    private var adapter: CollectAdapter = CollectAdapter()      // RecyclerView Adapter
    private lateinit var promptDialog: PromptDialog             // 加载提示框
    private var page: Int = 0                                   // 加载的页数
    private lateinit var infoDialog: InfoDialog
    private lateinit var beans: MutableList<CollectModel.Data.Datas>

    override fun layoutId(): Int = R.layout.activity_collect

    override fun getToolbar(): Toolbar? = toolbar

    override fun initView() {
        // set Adapter
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        promptDialog = PromptDialog.newInstance(this)
        infoDialog = InfoDialog.newInstance(this)

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastItemPostion = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastItemPostion == (itemCount - 1)) {
                        adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
                        fetchListData(lastItemPostion, true)
                    }
                }
            }
        })
        swipe_refresh.setOnRefreshListener {
            page = 0
            swipe_refresh.isRefreshing = false
            fetchListData(0, false)
        }

        // RecyclerView Adapter initial
        adapter.setOnItemClickListener(this)

        fetchListData(0, false)
    }

    fun fetchListData(lastPostion: Int, isLoadMore: Boolean) {
        if (!isLoadMore) {
            promptDialog.showLoadingPoint()
        }
        Api.getRetrofit()
            .create(CollectApi::class.java)
            .getCollects(page.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CollectModel> {
                override fun onError(e: Throwable?) {}

                override fun onNext(t: CollectModel?) {
                    if (t!!.errorCode == 0) {
                        beans = t.data.datas
                        val pageCount = t.data.pageCount
                        if (page <= pageCount) {
                            adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_COMPLETE)
                            page++
                        } else {
                            adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_END)
                        }
                        if (isLoadMore) {
                            adapter.addData(beans, lastPostion)
                        } else {
                            adapter.beans = beans
                            adapter.notifyDataSetChanged()
                        }
                        promptDialog.dismiss()
                    } else {
                        Toast.makeText(this@CollectActivity, t.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCompleted() {}
            })
    }

    /**
     * item点击事件
     */
    override fun onItemClick(bean: CollectModel.Data.Datas, position: Int) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("title", bean.title)
        intent.putExtra("url", bean.link)
        startActivity(intent)
    }

    override fun onItemLongClick(bean: CollectModel.Data.Datas, position: Int) {
        infoDialog.setInfo("")
            .setContent("是否取消收藏？")
            .setOnDialogClickListener { dialog, confirm ->
                if (confirm) {
                    CollectHelper.uncollect(this, bean.id)
                    beans.removeAt(position)
                    adapter.notifyItemRangeRemoved(position,1)
                } else {
                    dialog.dismiss()
                }
            }
            .build()
    }

}
