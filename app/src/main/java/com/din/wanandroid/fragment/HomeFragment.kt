package com.din.wanandroid.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.din.banner.limited.ScrollerPage
import com.din.wanandroid.R
import com.din.wanandroid.activities.MainActivity
import com.din.wanandroid.activities.WebActivity
import com.din.wanandroid.adapter.ArticleAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.CollectHelper
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.ArticleModel
import com.din.wanandroid.model.BannerModel
import com.din.wanandroid.model.BaseStateModel
import com.din.wanandroid.model.TopModel
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomeFragment : RecycleFragment(), SearchView.OnQueryTextListener,
    BaseAdapter.OnItemClickListener<ArticleModel.Datas> {

    private var adapter: ArticleAdapter = ArticleAdapter()       // RecyclerView Adapter

    private var bannerUrls: MutableList<String> = mutableListOf()
    private var bannerImages: MutableList<String> = mutableListOf()

    private lateinit var scrollerPage: ScrollerPage
    private lateinit var coordinator_layout: CoordinatorLayout
    private var isFirstStart = true

    override fun layoutId(): Int = R.layout.fragment_home

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    override fun onAfterCreateView(view: View) {
        val search_view = view.findViewById<SearchView>(R.id.search_view)

        val viewPager = view.findViewById(R.id.viewPager) as ViewPager
        val ll_point = view.findViewById(R.id.ll_point) as LinearLayout
        val iv_point = view.findViewById(R.id.iv_point) as ImageView
        coordinator_layout = view.findViewById(R.id.coordinator_layout)

        // Banner图初始化
        scrollerPage = ScrollerPage()
            .with(activity)
            .init(viewPager, ll_point, iv_point)
            .setItemClickListener {
                val intent = Intent(activity, WebActivity::class.java)
                intent.putExtra("title", bannerUrls.get(it))
                intent.putExtra("url", bannerUrls.get(it))
                startActivity(intent)
            }

        // RecyclerView Adapter initial
        adapter.setOnItemClickListener(this)

        // search view initial
        search_view.setIconifiedByDefault(false)     // 设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        search_view.queryHint = getString(R.string.search_query_hint)   // 设置输入框提示语
        search_view.setOnQueryTextListener(this)
        isFirstStart = false
        fetchData()
    }

    override fun onLastItem(lastPosition: Int) {
        adapter.setLoadingStatus(BaseAdapter.LOAD_STATUS_LOADING)
        fetchListData(lastPosition, true)
    }

    override fun swipeRefreshing() {
        fetchListData(0, false)
    }

    fun fetchData() {
        // 获取banner图
        Api.getService()
            .getBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseStateModel<MutableList<BannerModel>>> {
                override fun onError(e: Throwable?) {
                }

                override fun onNext(t: BaseStateModel<MutableList<BannerModel>>?) {
                    if (t!!.errorCode == 0) {
                        val datas = t.data
                        for (i in datas.indices) {
                            bannerImages.add(datas.get(i).imagePath)
                            bannerUrls.add(datas.get(i).url)
                        }
                        scrollerPage.setImage(bannerImages.toTypedArray()).start()
                        fetchListData(0, false)
                    }
                }

                override fun onCompleted() {
                }
            })
    }

    /**
     * 拉取置顶数据
     */
    fun fetchTopData() {
        Api.getService()
            .getTop()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseStateModel<MutableList<TopModel>>> {
                override fun onError(e: Throwable?) {
                }

                override fun onNext(t: BaseStateModel<MutableList<TopModel>>?) {
                    if (isFirstStart) {
                        if (t!!.errorCode == 0) {
                            val datas = t.data
//                            adapter.bean = datas
                            adapter.notifyDataSetChanged()
                            coordinator_layout.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(activity, t.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCompleted() {
                }
            })
    }

    /**
     * 拉取列表数据
     */
    fun fetchListData(lastPostion: Int, isLoadMore: Boolean) {
        Api.getService()
            .getArticle(page.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseStateModel<ArticleModel>> {
                override fun onError(e: Throwable?) {}

                override fun onNext(t: BaseStateModel<ArticleModel>?) {
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
                            (activity as MainActivity).promptDataBinding.dismiss()
                        }
                    } else {
                        Toast.makeText(activity, t.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCompleted() {}
            })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Toast.makeText(activity, newText, Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * item点击事件
     */
    override fun onItemClick(bean: ArticleModel.Datas, position: Int) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra("title", bean.title)
        intent.putExtra("url", bean.link)
        startActivity(intent)
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
}