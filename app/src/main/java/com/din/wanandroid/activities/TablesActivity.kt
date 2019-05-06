package com.din.wanandroid.activities

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.din.helper.dialog.PromptDialog
import com.din.wanandroid.R
import com.din.wanandroid.adapter.TabAdapter
import com.din.wanandroid.api.Api
import com.din.wanandroid.base.BaseActivity
import com.din.wanandroid.fragment.ProjectTabItemFragment
import com.din.wanandroid.model.BaseModel
import com.din.wanandroid.model.ProjectTypeModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tables.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TablesActivity : BaseActivity() {

    private lateinit var promptDialog: PromptDialog

    override fun layoutId(): Int = R.layout.activity_tables

    override fun getToolbar(): Toolbar? = toolbar

    override fun initView() {
        promptDialog = PromptDialog.newInstance(this)
        promptDialog.showLoadingPoint()
        Api.getService()
            .getProjectType()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseModel<MutableList<ProjectTypeModel>>> {
                override fun onError(e: Throwable?) {
                }

                override fun onNext(t: BaseModel<MutableList<ProjectTypeModel>>?) {
                    val datas = t!!.data
                    initTab(datas)
                }

                override fun onCompleted() {
                }
            })
    }

    private fun initTab(datas: MutableList<ProjectTypeModel>) {
        // Tab Title
        val titles: MutableList<String> = mutableListOf()
        val fragments: MutableList<Fragment> = arrayListOf()

        for (i in datas.indices) {
            titles.add(datas.get(i).name)
            fragments.add(ProjectTabItemFragment.newInstance(datas.get(i).id.toString()))
        }

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
        promptDialog.dismiss()
    }

}
