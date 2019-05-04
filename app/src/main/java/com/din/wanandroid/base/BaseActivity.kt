package com.din.wanandroid.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.din.helper.screen.ScreenHelper

/**
 * @author dinzhenyan
 * @date   2019-04-25 16:42
 * @IDE    Android Studio
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        // 设置Toolbar和返回按钮
        ScreenHelper.setToolBarAndHomeUp(this, getToolbar())
        initView()
    }

    protected open fun getToolbar(): Toolbar? {
        return null
    }

    abstract fun initView()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // 返回按钮点击事件
        ScreenHelper.setHomeUpAction(this, item)
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        // 结束activity之前先隐藏软键盘
        hideSoftKeyboard()
        super.finish()
    }

    /**
     * 隐藏软键盘
     */
    protected open fun hideSoftKeyboard() {
        val view = currentFocus
        view?.let {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}