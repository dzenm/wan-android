package com.din.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter : FragmentPagerAdapter {

    private var fragments: MutableList<Fragment>

    constructor(fm: FragmentManager, fragments: MutableList<Fragment>) : super(fm) {
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}