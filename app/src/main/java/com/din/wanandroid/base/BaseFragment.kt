package com.din.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open abstract class BaseFragment : Fragment() {

    protected val TAG = BaseFragment::class.java.simpleName

    protected var isViewInitiated = false               // 数据是否初始化
    protected var isViewCreated = false                 // 页面是否加载完成
    protected var isFetchPrepared = false
    protected var isFirstPage = false

    protected lateinit var rootView: View

    protected abstract fun layoutId(): Int              // 布局

    open fun initiatedView() {}                         // 初始化布局

    open fun lazyPrepareFetchData() {}                  // 懒加载数据

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = LayoutInflater.from(activity).inflate(layoutId(), container, false)
        rootView?.let { initiatedView() }               // 初始化控件
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true;                           // onViewCreate方法调用之后将标志设为true
        if (isFirstPage) {
            lazyPrepareFetchData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isViewCreated && !isFetchPrepared) {         // 页面可见并且onViewCreate()方法调用
            if (!isFirstPage) {                         // 调用懒加载数据方法
                lazyPrepareFetchData()
            }
            isFetchPrepared = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false;      // View被Destroy之后将标志设为false
    }
}