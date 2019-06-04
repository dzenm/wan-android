package com.din.wanandroid.api

import com.din.wanandroid.activities.App
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object Api {

    /**
     * 用于登录注册注销
     */
    fun getDefaultService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(ApiServices.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client())
            .build()
            .create(ApiServices::class.java)
    }

    /**
     * 用于返回json数据的api
     */
    fun getService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(ApiServices.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
            .create(ApiServices::class.java)
    }

    /**
     * okhttp设置
     */
    private fun client(): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.app))
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .writeTimeout(6, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }
}
