package com.din.wanandroid.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.din.wanandroid.R
import com.din.wanandroid.activities.MainActivity
import com.din.wanandroid.adapter.TabAdapter
import com.din.wanandroid.base.BaseFragment
import com.google.android.material.tabs.TabLayout

class HallFragment : BaseFragment() {

    private lateinit var treeFragment: TreeFragment
    private lateinit var naviFragment: NaviFragment
    private lateinit var projectFragment: ProjectFragment

    override fun layoutId(): Int = R.layout.fragment_hall

    override fun lazyPrepareFetchData() {
        (activity as MainActivity).promptDataBinding.showLoadingPoint()
        // Tab Title
        val titles: Array<String> = arrayOf(
            getString(R.string.tab_item_tree),
            getString(R.string.tab_item_navi),
            getString(R.string.tab_item_project)
        )

        val tab_layout = rootView.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)

        val fragments: MutableList<Fragment> = arrayListOf()

        // fragment initial
        treeFragment = TreeFragment()
        naviFragment = NaviFragment()
        projectFragment = ProjectFragment()

        fragments.add(treeFragment)
        fragments.add(naviFragment)
        fragments.add(projectFragment)

        // viewpager and tablayout fixed
        val adapter = TabAdapter(childFragmentManager, fragments, titles.toList())
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)
    }

}
