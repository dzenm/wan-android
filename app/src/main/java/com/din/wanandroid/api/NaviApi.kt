package com.din.wanandroid.api

import com.din.wanandroid.model.NaviModel
import retrofit2.http.GET
import rx.Observable

interface NaviApi {

    @GET("navi/json")
    fun getNavi(): Observable<NaviModel>
}