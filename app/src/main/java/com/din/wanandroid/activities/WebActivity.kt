package com.din.wanandroid.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.din.wanandroid.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title")
        supportActionBar?.setTitle(title)
        val url = intent.getStringExtra("url")
        url?.let { web_view.loadUrl(it) }

        web_view.webChromeClient = WebChromeClient()    // 此方法可以在webview中打开链接而不会跳转到外部浏览器

        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true            // 开启JavaScript支持

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
