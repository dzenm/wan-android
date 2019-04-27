package com.din.wanandroid.activities

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import com.din.wanandroid.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    private lateinit var web_view: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 避免WebView内存泄露。不在xml中定义 Webview ，而是在需要的时候在Activity中创建，并且Context使用 getApplicationgContext()
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        web_view = WebView(applicationContext)
        web_view?.setLayoutParams(params)
        root_view.addView(web_view)

        val titleStr = intent.getStringExtra("title")
        supportActionBar?.setTitle(titleStr)
        val url = intent.getStringExtra("url")
        url?.let { web_view.loadUrl(it) }

        // 此方法可以在webview中打开链接而不会跳转到外部浏览器
        web_view.webViewClient = object : WebViewClient() {

            // 在开始加载网页时会回调
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            }

            // 加载错误的时候会回调
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            }

            // 加载完成的时候会回调
            override fun onPageFinished(view: WebView?, url: String?) {
            }

        }

        // 设置WebChromeClient类
        web_view.webChromeClient = object : WebChromeClient() {

            // 获得网页的加载进度并显示
            override fun onProgressChanged(
                view: WebView?,
                newProgress: Int
            ) {
                if (newProgress >= 0 && newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                } else {
                    progressBar.visibility = View.GONE
                }
            }

            // 获取Web页中的标题
            override fun onReceivedTitle(
                view: WebView?,
                title: String?
            ) {
                supportActionBar?.setTitle(title)
            }

            // 支持javascript的警告框
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                message?.let { toast(it, Toast.LENGTH_SHORT).show() }
                return true
            }

            // 支持javascript的确认框
            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                AlertDialog.Builder(this@WebActivity)
                    .setTitle("是否确定？")
                    .setMessage(message)
                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                        result?.confirm()
                    })
                    .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                        result?.cancel()
                    })
                    .setCancelable(false)
                    .show()
                // 返回布尔值：判断点击时确认还是取消。true表示点击了确认；false表示点击了取消；
                return true
            }

            // 支持javascript输入框
            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                val editText = EditText(this@WebActivity)
                editText.setText(defaultValue)
                AlertDialog.Builder(this@WebActivity)
                    .setTitle(message)
                    .setView(editText)
                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                        result?.confirm(editText.getText().toString())
                    })
                    .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                        result?.cancel()
                    })
                    .setCancelable(false)
                    .show()
                return true
            }


        }

        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true            // 开启JavaScript支持

        // 设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true              // 将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true         // 缩放至屏幕的大小

        // 缩放操作
        webSettings.setSupportZoom(true)                // 支持缩放，默认为true。是下面那个的前提
        webSettings.builtInZoomControls = true          // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false         // 隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK     // 关闭webview中缓存
        webSettings.allowFileAccess = true                              // 设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true        // 支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true                     // 支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"                   // 设置编码格式

        webSettings.domStorageEnabled = true                            // 开启 DOM storage API 功能
        webSettings.databaseEnabled = true                              // 开启 database storage API 功能
        webSettings.setAppCacheEnabled(true)                            // 开启 Application Caches 功能
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

    override fun onDestroy() {
        // 在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
        if (web_view != null) {
            web_view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            web_view.clearHistory()

            (web_view.getParent() as ViewGroup).removeView(web_view)
            web_view.destroy()
        }
        super.onDestroy()
    }
}
