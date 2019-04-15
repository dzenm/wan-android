package com.din.wanandroid.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open abstract class BaseFragment : Fragment() {

    private val TAG = javaClass.simpleName

    protected var isViewCreated = false
    protected var isFetchPrepared = false
    protected lateinit var rootView: View

    protected abstract fun initData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated && userVisibleHint && !isFetchPrepared) {
            isFetchPrepared = true
            initData()
        }
    }
}