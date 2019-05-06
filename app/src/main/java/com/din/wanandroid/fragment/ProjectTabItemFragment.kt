package com.din.wanandroid.fragment

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.activities.WebActivity
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.CollectHelper
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.ArticleModel
import com.din.wanandroid.model.BaseModel
import kotlinx.android.synthetic.main.fragment_home.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author dinzhenyan
 * @date   2019-04-26 14:36
 * @IDE    Android Studio
 */
class ProjectTabItemFragment(var typeId: String) : RecycleFragment(),
    BaseAdapter.OnItemClickListener<ArticleModel.Datas> {

    companion object {
        fun newInstance(typeId: String): ProjectTabItemFragment {
            val tabItemFragment = ProjectTabItemFragment(typeId)
            return tabItemFragment
        }
    }

    private var adapter: ArticleAdapter = ArticleAdapter()

    override fun layoutId(): Int = R.layout.fragment_tab_item

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    override fun onAfterCreateView(view: View) {
        // RecyclerView Adapter initial
        adapter.setOnItemClickListener(this)
        fetchListData(1, false)
    }

    override fun onLastItem(lastPosition: Int) {
        adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
        fetchListData(lastPosition, true)
    }

    override fun swipeRefreshing() {
        fetchListData(1, false)
    }

    override fun lazyPrepareFetchData() {
        fetchListData(1, false)
    }

    /**
     * 拉取列表数据
     */
    fun fetchListData(lastPostion: Int, isLoadMore: Boolean) {
        Api.getService()
            .getProject(page.toString(), typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseModel<ArticleModel>> {
                override fun onError(e: Throwable?) {}

                override fun onNext(t: BaseModel<ArticleModel>?) {
                    if (t!!.errorCode == 0) {
                        val datas = t.data.datas
                        val pageCount = t.data.pageCount
                        if (page <= pageCount) {
                            adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_COMPLETE)
                            page++
                        } else {
                            adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_END)
                        }
                        if (isLoadMore) {
                            adapter.addData(datas, lastPostion)
                        } else {
                            adapter.beans = datas
                            adapter.notifyDataSetChanged()
                            coordinator_layout.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(activity, t.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCompleted() {}
            })
    }

    override fun onItemCollectClick(bean: ArticleModel.Datas, position: Int) {
        if (bean.collect) {
            activity?.let { CollectHelper.uncollect(it, bean.id) }
            bean.collect = false
        } else {
            activity?.let { CollectHelper.collect(it, bean.id) }
            bean.collect = true
        }
    }

    override fun onItemClick(bean: ArticleModel.Datas, position: Int) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra("title", bean.title)
        intent.putExtra("url", bean.link)
        startActivity(intent)
    }
}