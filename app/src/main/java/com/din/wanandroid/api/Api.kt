package com.din.wanandroid.api

import com.din.wanandroid.activities.App
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Api {

    fun getDefaultService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(ApiServices.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client())
            .build()
            .create(ApiServices::class.java)
    }

    fun getService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(ApiServices.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
            .create(ApiServices::class.java)
    }

    private fun client(): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.app))
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build()
        return okHttpClient
    }
}
