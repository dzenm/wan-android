package com.dzenm.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter(
    fragmentManager: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: List<String>
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragments.get(position)


    override fun getCount(): Int = fragments.size


    override fun getPageTitle(position: Int): CharSequence = titles.get(position)
}