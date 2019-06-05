package com.dzenm.wanandroid.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.dzenm.helper.log.Logger
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.adapter.TabAdapter
import com.dzenm.wanandroid.base.BaseFragment
import com.google.android.material.tabs.TabLayout

class HallFragment : BaseFragment() {

    private lateinit var projectFragment: ProjectFragment
    private lateinit var naviFragment: NaviFragment
    private lateinit var treeFragment: TreeFragment

    override fun layoutId(): Int = R.layout.fragment_hall

    override fun lazyPrepareFetchData() {
        // Tab Title
        val titles: Array<String> = arrayOf(
            getString(R.string.tab_item_project),
            getString(R.string.tab_item_navi),
            getString(R.string.tab_item_tree)
        )

        val tab_layout = rootView.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)

        val fragments: MutableList<Fragment> = arrayListOf()

        // fragment initial
        projectFragment = ProjectFragment()
        naviFragment = NaviFragment()
        treeFragment = TreeFragment()

        fragments.add(projectFragment)
        fragments.add(naviFragment)
        fragments.add(treeFragment)

        // viewpager and tablayout fixed
        val adapter = TabAdapter(childFragmentManager, fragments, titles.toList())
        viewPager.offscreenPageLimit = titles.size
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)
        Logger.d(TAG + "---初始化完成")
    }

}
