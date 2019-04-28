package com.din.wanandroid.base

import android.os.Bundle
import android.view.MenuItem
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

        ScreenHelper.setToolBarAndHomeUp(this, getToolbar())
        initView()
    }

    protected open fun getToolbar(): Toolbar? {
        return null
    }

    abstract fun initView()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        ScreenHelper.setHomeUpAction(this, item)
        return super.onOptionsItemSelected(item)
    }
}