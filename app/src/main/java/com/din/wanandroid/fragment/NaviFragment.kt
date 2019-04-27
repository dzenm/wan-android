package com.din.wanandroid.fragment

import com.din.wanandroid.R
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.base.BaseFragment

class NaviFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_project

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

}
