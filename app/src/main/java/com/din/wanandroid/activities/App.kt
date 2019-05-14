package com.din.wanandroid.activities

import android.app.Application
import com.din.helper.log.CrashHandler
import com.din.wanandroid.util.SpHelper

class App : Application() {

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        CrashHandler.getInstance().init(this)
        SpHelper.getInstance().init(this, "shared_pref_login")
    }
}