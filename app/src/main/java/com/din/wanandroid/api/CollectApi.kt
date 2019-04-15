package com.din.wanandroid.api

import com.din.wanandroid.model.ArticleModel
import com.din.wanandroid.model.SuccessModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

interface CollectApi {

    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: String): Observable<SuccessModel>

    @POST("lg/uncollect_originId/{id}/json")
    fun uncollectArticle(@Path("id") id: String): Observable<SuccessModel>

    @GET("lg/collect/list/{id}/json")
    fun getCollects(@Path("id") id: String): Observable<ArticleModel>
}