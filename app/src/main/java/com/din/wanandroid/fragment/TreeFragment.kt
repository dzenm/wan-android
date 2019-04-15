package com.din.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.TreeApi
import com.din.wanandroid.model.TreeModel
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TreeFragment : RecycleFragment() {

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter
    private var isFirstStart = true

    override fun layoutId(): Int = R.layout.fragment_tree

    override fun scrollToLastVisibleItem(lastPosition: Int) {
    }

    override fun swipeToRefresh() {
    }

    override fun onAfterCreateView(view: View) {
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    override fun initData() {
        promptDialog.showLoadingPoint()
        Api.getRetrofit()
            .create(TreeApi::class.java)
            .getAllTrees()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<TreeModel> {
                override fun onError(e: Throwable?) {
                }

                override fun onNext(t: TreeModel?) {
                    Log.d("DZY", t!!.data.get(0).name)
                    promptDialog.dismiss()
                }

                override fun onCompleted() {
                }
            })
    }
}
