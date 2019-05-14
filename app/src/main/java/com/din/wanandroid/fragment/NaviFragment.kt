package com.din.wanandroid.fragment

import android.view.View
import com.din.banner.testscroller.ScrollerPage
import com.din.wanandroid.R
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.base.BaseFragment


class NaviFragment : BaseFragment() {

    private val images = intArrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.a, R.drawable.b)

    override fun layoutId(): Int = R.layout.fragment_project

    override fun lazyPrepareFetchData() {
        super.lazyPrepareFetchData()
        initiatedView(rootView)
    }

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    protected fun initiatedView(view: View) {
        val scrollerPage = view.findViewById<ScrollerPage>(R.id.scrollerPage)
        scrollerPage.setImage(images).build()    // 轮播图显示
    }

}
