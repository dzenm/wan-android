package com.din.wanandroid.api

import com.din.wanandroid.model.ArticleModel
import com.din.wanandroid.model.BannerModel
import com.din.wanandroid.model.TopModel
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface HomeApi {

    @GET("article/list/{id}/json")
    fun getArticle(@Path("id") id: String): Observable<ArticleModel>

    @GET("banner/json")
    fun getBanner(): Observable<BannerModel>

    @GET("article/top/json")
    fun getTop(): Observable<TopModel>
}