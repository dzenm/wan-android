package com.din.wanandroid.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

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

        getToolbar()?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        initView()
    }

    protected open fun getToolbar(): Toolbar? {
        return null
    }

    abstract fun initView()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}