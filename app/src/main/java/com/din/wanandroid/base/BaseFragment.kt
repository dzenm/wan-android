package com.din.wanandroid.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open abstract class BaseFragment : Fragment() {

    protected val TAG = BaseFragment::class.java.simpleName

    protected var isViewInitiated = false               // 数据是否初始化
    protected var isViewCreated = false                 // 页面是否加载完成
    protected var isFetchPrepared = false               // 是否进行了懒加载

    protected lateinit var rootView: View

    protected abstract fun layoutId(): Int              // 布局

    open fun initiatedView() {}                         // 初始化布局

    open fun lazyPrepareFetchData() {}                  // 懒加载数据

    open fun getFirstPage(): Boolean = false            // 只有在一组Fragment切换的时候，加载第一个的时候不能进行懒加载

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = LayoutInflater.from(activity).inflate(layoutId(), container, false)
        rootView?.let { initiatedView() }               // 初始化控件
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true                            // onViewCreate方法调用之后将标志设为true
        if (getFirstPage()) {
            lazyPrepareFetchData()
            isFetchPrepared = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated && isVisibleToUser) {         // 是否调用onViewCreated并且页面是否可见
            if (!isFetchPrepared) {                     // 是否加载过数据
                if (!getFirstPage()) {                  // 调用懒加载数据方法
                    lazyPrepareFetchData()
                    isFetchPrepared = true
                }
            } else {                                    // Fragment从不可见到可见，但是Fragment是已经创建之后
                onRestart()
            }
        }
    }

    /**
     * 页面已经创建，从一个可见的Fragment切换到不可见的Fragment（不可见的Fragment为本Fragment）
     */
    protected fun onRestart() {

    }

    /**
     * 跳转页面
     */
    protected fun navigation(clazz: Class<Any>) {
        val intent = Intent(activity, clazz)
        activity?.startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false                           // View被Destroy之后将标志设为false
        isFetchPrepared = false
    }
}