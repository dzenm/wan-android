package com.din.wanandroid.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dzenm.helper.screen.ScreenHelper

/**
 * @author dinzhenyan
 * @date   2019-04-25 16:42
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var dataBinding: ViewDataBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isUseDataBinding()) {
            dataBinding = DataBindingUtil.setContentView(this, layoutId())
        } else {
            setContentView(layoutId())
        }

        initialView()
    }

    /**
     * 是否使用databinding，默认使用
     */
    protected open fun isUseDataBinding(): Boolean = true

    /**
     * 设定布局
     */
    protected abstract fun layoutId(): Int

    /**
     * 初始化View相关
     */
    abstract fun initialView()

    /**
     * 设置Toolbar和返回按钮
     */
    protected open fun setToolbar(toolbar: Toolbar) {
        ScreenHelper.setToolBarAndHomeUp(this, toolbar)
    }

    /**
     * 是否开启左上角的返回键功能
     */
    protected open fun isEnabledHomeUpAsButton(): Boolean = true

    /**
     *
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        ScreenHelper.setHomeUpAction(this, item)             // 返回按钮点击事件
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        hideSoftKeyboard()                                          // 结束activity之前先隐藏软键盘
        super.finish()
    }

    /**
     * 重写返回键时，调用此方法可以时应用在点击返回时进入到后台任务
     */
    protected open fun moveTaskToBack() {
        if (!moveTaskToBack(false)) {
            super.onBackPressed()
        }
    }

    /**
     * 跳转到下一个Activity
     */
    protected open fun navigation(clazz: Class<Any>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    /**
     * 隐藏软键盘
     */
    protected open fun hideSoftKeyboard() {
        val view = currentFocus
        view?.let {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}