package com.din.wanandroid.fragment

import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.din.banner.unlimited.ScrollerPage
import com.din.wanandroid.R
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.base.BaseFragment


class NaviFragment : BaseFragment() {

    private val images = intArrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d)
    private var scrollerPage: ScrollerPage? = null

    protected fun initiatedView(view: View) {
        val viewPager = view.findViewById(R.id.viewPager) as ViewPager
        val pointView = view.findViewById(R.id.pointView) as LinearLayout

//        pointView!!.setOnClickListener { }
        scrollerPage = ScrollerPage.newInstance()
        scrollerPage!!.init(activity, viewPager, pointView)    // 轮播图初始化
        scrollerPage!!.setImages(images).show()    // 轮播图显示
    }

    override fun layoutId(): Int = R.layout.fragment_project

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    override fun lazyPrepareFetchData() {
        super.lazyPrepareFetchData()
        initiatedView(rootView)
    }

}
