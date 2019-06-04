package com.din.wanandroid.fragment

import android.view.View
import com.din.wanandroid.R
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.base.BaseFragment


class NaviFragment : BaseFragment() {

    private val images = intArrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.a, R.drawable.b)

    override fun layoutId(): Int = R.layout.fragment_tree

    override fun lazyPrepareFetchData() {
        super.lazyPrepareFetchData()
        initiatedView(rootView)
    }

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    protected fun initiatedView(view: View) {

    }

}
