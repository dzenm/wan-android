package com.din.wanandroid.activities

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.din.wanandroid.R
import com.din.wanandroid.adapter.ViewPagerAdapter
import com.din.wanandroid.base.BaseActivity
import com.din.wanandroid.fragment.HallFragment
import com.din.wanandroid.fragment.HomeFragment
import com.din.wanandroid.fragment.PersonalFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private lateinit var fragments: MutableList<Fragment>
    private lateinit var homeFragment: HomeFragment
    private lateinit var hallFragment: HallFragment
    private lateinit var personalFragment: PersonalFragment

    override fun layoutId(): Int = R.layout.activity_main

    override fun isUseDataBinding(): Boolean = false

    override fun initialView() {
        promptDataBinding.showLoadingPoint()
        fragments = mutableListOf()

        homeFragment = HomeFragment()
        hallFragment = HallFragment()
        personalFragment = PersonalFragment()

        fragments.add(homeFragment)
        fragments.add(hallFragment)
        fragments.add(personalFragment)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments) // ViewPager Adapter
        viewPager.offscreenPageLimit = fragments.size       // ViewPager 预先加载的个数
        viewPager.addOnPageChangeListener(this)
        bottom_navigation_view.selectedItemId = R.id.home   // 设置第一个为选中

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> viewPager.currentItem = 0
                R.id.hall -> viewPager.currentItem = 1
                R.id.personal -> viewPager.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        moveTaskToBack()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) {
        val menuItem = bottom_navigation_view.menu.getItem(position)
        menuItem.setChecked(true)
    }
}