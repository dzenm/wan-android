package com.dzenm.wanandroid.activities

import android.app.Application
import com.dzenm.helper.log.CrashHandler
import com.dzenm.helper.log.Logger
import com.dzenm.wanandroid.util.SpHelper

class App : Application() {

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        CrashHandler.getInstance().init(this)
        SpHelper.getInstance().init(this, "shared_pref_login")
        Logger.getInstance().setDebug(true)
    }
}