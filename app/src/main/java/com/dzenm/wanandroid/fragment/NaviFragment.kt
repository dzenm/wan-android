package com.dzenm.wanandroid.fragment

import android.view.View
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.adapter.ArticleAdapter
import com.dzenm.wanandroid.base.BaseFragment


class NaviFragment : BaseFragment() {

    private val images = intArrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.a, R.drawable.b)

    override fun layoutId(): Int = R.layout.fragment_hall_item

    override fun lazyPrepareFetchData() {
        super.lazyPrepareFetchData()
        initiatedView(rootView)
    }

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    protected fun initiatedView(view: View) {

    }

}
