package com.din.wanandroid.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.*

/**
 * Created by dinzhenyan on 2018/3/29.
 */

class FragmentManager {
    private lateinit var activity: AppCompatActivity
    private lateinit var fragment: Fragment
    private var ID: Int = 0
    private var list: MutableList<Fragment>

    constructor(activity: AppCompatActivity, ID: Int, list: MutableList<Fragment>) {
        this.activity = activity
        this.ID = ID
        this.list = list
    }

    constructor(fragment: Fragment, ID: Int, list: MutableList<Fragment>) {
        this.fragment = fragment
        this.ID = ID
        this.list = list
    }

    constructor(activity: AppCompatActivity, ID: Int) {
        this.activity = activity
        this.ID = ID
        list = ArrayList()
    }

    constructor(fragment: Fragment, ID: Int) {
        this.fragment = fragment
        this.ID = ID
        list = ArrayList()
    }

    fun addFragment() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        for (i in list!!.indices) {
            transaction.add(ID, list!![i])
        }
        transaction.commitAllowingStateLoss()
    }

    fun addChildFragment() {
        val transaction = fragment!!.childFragmentManager.beginTransaction()
        for (i in list!!.indices) {
            transaction.add(ID, list!![i], i.toString())
        }
        transaction.commitAllowingStateLoss()
    }

    fun showFragment(currentFragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        for (i in list!!.indices) {
            transaction.hide(list!![i])
        }
        transaction.show(currentFragment)
        transaction.commitAllowingStateLoss()
    }

    fun showChildFragment(currentFragment: Fragment) {
        val transaction = fragment?.childFragmentManager.beginTransaction()
        for (i in list!!.indices) {
            transaction.hide(list!![i])
        }
        transaction.show(currentFragment)
        transaction.commitAllowingStateLoss()
    }

    fun show(currentFragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        if (currentFragment.isAdded) {
            for (i in list!!.indices) {
                transaction.hide(list!![i])
            }
            transaction.show(currentFragment)
        } else {
            transaction.add(ID, currentFragment)
            list!!.add(currentFragment)
        }
        transaction.commitAllowingStateLoss()
    }
}