package com.dzenm.wanandroid.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.din.banner.limited.ScrollerPage
import com.dzenm.helper.log.Logger
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.activities.WebActivity
import com.dzenm.wanandroid.adapter.ArticleAdapter
import com.dzenm.wanandroid.api.Api
import com.dzenm.wanandroid.api.ApiServicesHelper
import com.dzenm.wanandroid.api.CollectHelper
import com.dzenm.wanandroid.base.BaseAdapter
import com.dzenm.wanandroid.model.ArticleModel
import com.dzenm.wanandroid.model.BannerModel
import com.dzenm.wanandroid.model.TopModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : RecycleFragment(), SearchView.OnQueryTextListener,
    BaseAdapter.OnItemClickListener<ArticleModel.Datas> {

    private var mAdapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    private var mBannerUrls: MutableList<String> = mutableListOf()
    private var mBannerImages: MutableList<String> = mutableListOf()

    private lateinit var mScrollerPage: ScrollerPage             // Banner图片
    private var isFirstStart = true                             // 是否是第一次启动

    override fun layoutId(): Int = R.layout.fragment_home

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = mAdapter

    override fun onAfterCreateView(view: View) {
        val search_view = view.findViewById<SearchView>(R.id.search_view)

        // 与Banner图片显示相关
        val viewPager = view.findViewById(R.id.viewPager) as ViewPager
        val ll_point = view.findViewById(R.id.ll_point) as LinearLayout
        val iv_point = view.findViewById(R.id.iv_point) as ImageView

        // Banner图初始化
        mScrollerPage = ScrollerPage().with(activity).init(viewPager, ll_point, iv_point)
            .setItemClickListener {
                val intent = Intent(activity, WebActivity::class.java)
                intent.putExtra(WebActivity.TITLE, mBannerUrls.get(it))
                intent.putExtra(WebActivity.URL, mBannerUrls.get(it))
                startActivity(intent)
            }

        // RecyclerView Adapter initial
        mAdapter.setItemOnClickListener(this)

        // search view initial
        search_view.setIconifiedByDefault(false)     // 设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        search_view.queryHint = getString(R.string.search_query_hint)      // 设置输入框提示语
        search_view.setOnQueryTextListener(this)
        isFirstStart = false
        swipe_refresh.isRefreshing = true
        getBannerData()                              // 加载banner图片数据
    }

    override fun onLastItem(lastPosition: Int) {
        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
        getArticleListData(lastPosition, true)
    }

    override fun swipeRefreshing() {
        getArticleListData(0, false)
    }

    fun getBannerData() {
        // 获取banner图片
        ApiServicesHelper<MutableList<BannerModel>>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<BannerModel>>() {
                override fun onNext(data: MutableList<BannerModel>) {
                    for (i in data.indices) {
                        mBannerImages.add(data.get(i).imagePath)
                        mBannerUrls.add(data.get(i).url)
                    }
                    // 设置banner图片显示
                    iv_empty_pic.visibility = View.GONE
                    mScrollerPage.setImage(mBannerImages.toTypedArray()).start()
                    Logger.d(TAG + "---Banner图片数据加载完成")
                    getArticleListData(0, false)
                }
            }).request(Api.getService().getBanner())
    }

    /**
     * 获取置顶数据
     */
    fun getTopArticleData() {
        ApiServicesHelper<MutableList<TopModel>>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<TopModel>>() {
                override fun onNext(data: MutableList<TopModel>) {
                    if (isFirstStart) {
//                    mAdapter.bean = data
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }).request(Api.getService().getTop())
    }

    /**
     * 获取列表数据
     */
    fun getArticleListData(lastPostion: Int, isLoadMore: Boolean) {
        ApiServicesHelper<ArticleModel>(activity as AppCompatActivity)
            .setOnCallback(object : ApiServicesHelper.OnCallback<ArticleModel>() {
                override fun onNext(data: ArticleModel) {
                    val datas = data.datas
                    val pageCount = data.pageCount
                    if (mPage <= pageCount) {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_COMPLETE)
                        mPage++
                    } else {
                        mAdapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_END)
                    }
                    if (isLoadMore) {
                        mAdapter.addData(datas, lastPostion)
                    } else {
                        mAdapter.mBeans = datas
                        mAdapter.notifyDataSetChanged()

                        recycler_view.visibility = View.VISIBLE
                        swipe_refresh.isRefreshing = false
                    }
                    Logger.d(TAG + "---文章数据加载完成: " + datas)
                }
            }).request(Api.getService().getArticle(mPage))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Logger.d(TAG + "---onQueryTextSubmit: " + query)
        Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Logger.d(TAG + "---onQueryTextChange: " + newText)
        Toast.makeText(activity, newText, Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * item点击事件
     */
    override fun onItemClick(bean: ArticleModel.Datas, position: Int) {
        Logger.d(TAG + "---onItemClick position: " + position)
        Logger.d(TAG + "---onItemClick bean: " + bean)
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra(WebActivity.TITLE, bean.title)
        intent.putExtra(WebActivity.URL, bean.link)
        startActivity(intent)
    }

    /**
     * 收藏点击事件
     */
    override fun onItemCollectClick(bean: ArticleModel.Datas, position: Int) {
        if (bean.collect) {
            activity?.let { CollectHelper.uncollect(it, bean.id) }
            bean.collect = false
        } else {
            activity?.let { CollectHelper.collect(it, bean.id) }
            bean.collect = true
        }
    }
}