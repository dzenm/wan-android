package com.din.wanandroid.activities

import android.util.Log
import androidx.fragment.app.Fragment
import com.din.wanandroid.R
import com.din.wanandroid.adapter.MultipleAdapter
import com.din.wanandroid.adapter.TabAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.api.ApiServicesHelper
import com.din.wanandroid.base.BaseActivity
import com.din.wanandroid.fragment.ProjectTabFragment
import com.din.wanandroid.fragment.TreeTabFragment
import com.din.wanandroid.fragment.WxTabFragment
import com.din.wanandroid.model.ProjectTypeModel
import com.din.wanandroid.model.TreeModel
import com.din.wanandroid.model.WxModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tables.*

class TabActivity : BaseActivity() {

    companion object {
        const val BEAN_TYPE = "bean_type"
        const val BEAN_POSITION = "bean_position"

        const val TYPE_PROJECT = 1001
        const val TYPE_WX = 1002
        const val TYPE_TREE = 1003
    }

    private var mType: Int = 0
    private var mPositionId = 0

    override fun layoutId(): Int = R.layout.activity_tables

    override fun initialView() {
        setToolbar(toolbar)
        mType = intent.getIntExtra(BEAN_TYPE, 0)
        mPositionId = intent.getIntExtra(BEAN_POSITION, 0)

        if (mType == MultipleAdapter.TYPE_PROJECT) {
            getProjectTypeData()
        } else if (mType == MultipleAdapter.TYPE_WX) {
            getWxData()
        } else if (mType == MultipleAdapter.TYPE_TREE) {
            getTreeData()
        }
    }

    /**
     * 获取项目类型数据
     */
    fun getProjectTypeData() {
        ApiServicesHelper<MutableList<ProjectTypeModel>>(this)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<ProjectTypeModel>>() {
                override fun onNext(data: MutableList<ProjectTypeModel>) {
                    Log.d("DZY", "data: " + data.size)
                    // Tab Title
                    val titles: MutableList<String> = mutableListOf()
                    val fragments: MutableList<Fragment> = arrayListOf()

                    for (i in data.indices) {
                        titles.add(data.get(i).name)
                        if (i == 0) {
                            fragments.add(ProjectTabFragment.newInstance(data.get(i).id, true, TYPE_PROJECT))
                        } else {
                            fragments.add(ProjectTabFragment.newInstance(data.get(i).id, false, TYPE_PROJECT))
                        }
                    }
                    initTab(titles, fragments)
                }
            }).request(Api.getService().getProjectType())
    }

    /**
     * 获取微信公众号的数据
     */
    fun getWxData() {
        ApiServicesHelper<MutableList<WxModel>>(this)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<WxModel>>() {
                override fun onNext(data: MutableList<WxModel>) {
                    // Tab Title
                    val titles: MutableList<String> = mutableListOf()
                    val fragments: MutableList<Fragment> = arrayListOf()

                    for (i in data.indices) {
                        titles.add(data.get(i).name)
                        // 根据标题添加Fragment
                        val id = data.get(i).id
                        if (i == 0) {
                            fragments.add(WxTabFragment.newInstance(id, true, TYPE_WX))
                        } else {
                            fragments.add(WxTabFragment.newInstance(id, false, TYPE_WX))
                        }
                        // 跳转的Tab位置
                        if (mPositionId == id) {
                            mPositionId = i
                        }
                    }
                    initTab(titles, fragments)
                }
            }).request(Api.getService().getWx())
    }

    /**
     * 获取体系的数据
     */
    fun getTreeData() {
        ApiServicesHelper<MutableList<TreeModel>>(this)
            .setOnCallback(object : ApiServicesHelper.OnCallback<MutableList<TreeModel>>() {
                override fun onNext(data: MutableList<TreeModel>) {
                    // Tab Title
                    val titles: MutableList<String> = mutableListOf()
                    val fragments: MutableList<Fragment> = arrayListOf()

                    // 找到点击的ITEM所在的位置
                    var position = 0
                    for (i in data.indices) {
                        val child = data.get(i).children
                        for (j in child.indices) {
                            val id = child.get(j).id
                            if (mPositionId == id) {
                                mPositionId = j
                                position = i
                                break
                            }
                        }
                    }

                    // 添加数据显示
                    val child = data.get(position).children
                    for (i in child.indices) {
                        titles.add(child.get(i).name)
                        // 跳转的Tab位置
                        val id = child.get(i).id
                        // 根据标题添加Fragment
                        if (i == 0) {
                            fragments.add(TreeTabFragment.newInstance(id, true, TYPE_TREE))
                        } else {
                            fragments.add(TreeTabFragment.newInstance(id, false, TYPE_TREE))
                        }
                    }
                    initTab(titles, fragments)
                }
            }).request(Api.getService().getTree())
    }

    private fun initTab(titles: MutableList<String>, fragments: MutableList<Fragment>) {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
            }
        })

        // viewpager and tablayout fixed
        val adapter = TabAdapter(supportFragmentManager, fragments, titles.toList())
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = titles.size
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(viewPager)
        // 跳转选中的Tab
        if (mPositionId != 0) {
            tab_layout.getTabAt(mPositionId)?.select()
        }
    }
}
