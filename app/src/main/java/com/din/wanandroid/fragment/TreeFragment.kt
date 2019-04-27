package com.din.wanandroid.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.activities.TablesActivity
import com.din.wanandroid.activities.WebActivity
import com.din.wanandroid.adapter.MultipleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.ProjectApi
import com.din.wanandroid.model.MultipleTitleBean
import com.din.wanandroid.model.NewProjectModel
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TreeFragment : RecycleFragment(), MultipleAdapter.OnItemClickListener {

    private var adapter: MultipleAdapter = MultipleAdapter()       // RecyclerView Adapter
    private var beans: MutableList<Any> = mutableListOf()

    override fun layoutId(): Int = R.layout.fragment_tree

    override fun scrollToLastVisibleItem(lastPosition: Int) {

    }

    override fun swipeToRefresh() {
        page = 0
        fetchData()
    }

    override fun onAfterCreateView(view: View) {
        adapter.setOnItemClickListener(this)
        fetchData()
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    fun fetchData() {
        promptDialog.showLoadingPoint()
        Api.getRetrofit()
            .create(ProjectApi::class.java)
            .getNewProject(page.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<NewProjectModel> {
                override fun onError(e: Throwable?) {
                }

                override fun onNext(t: NewProjectModel?) {
                    val datas = t!!.data.datas
                    beans.add(MultipleTitleBean("项目"))
                    beans.addAll(datas)
                    adapter.addData(beans)
                    promptDialog.dismiss()
                }

                override fun onCompleted() {
                }
            })
    }

    override fun onItemClick(position: Int) {
        if (beans.get(position) is MultipleTitleBean) {
            val intent = Intent(activity, TablesActivity::class.java)
            startActivity(intent)
        } else if (beans.get(position) is NewProjectModel.Data.Datas) {
            val bean = beans.get(position) as NewProjectModel.Data.Datas
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra("title", bean.title)
            intent.putExtra("url", bean.projectLink)
            startActivity(intent)
        }
    }
}
