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

    fun getDefaultRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UserApi.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client())
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UserApi.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
    }

    private fun client(): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.app))
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build()
        return okHttpClient
    }
}
