package com.din.wanandroid.api

import com.din.wanandroid.model.*
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

/**
 * @author dinzhenyan
 * @date   2019-05-05 22:45
 * @IDE    Android Studio
 */
interface ApiServices {

    companion object {
        val BASE_URL = "https://www.wanandroid.com/"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<String>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") username: String, @Field("password") password: String, @Field("repassword") repassword: String): Call<String>

    /**
     * 注销
     */
    @GET("user/logout/json")
    fun logout(): Call<String>

    /**
     * 获取Banner图列表
     */
    @GET("banner/json")
    fun getBanner(): Observable<BaseStateModel<MutableList<BannerModel>>>

    /**
     * 获取所有文章
     */
    @GET("article/list/{id}/json")
    fun getArticle(@Path("id") id: String): Observable<BaseStateModel<ArticleModel>>

    /**
     * 获取置顶文章
     */
    @GET("article/top/json")
    fun getTop(): Observable<BaseStateModel<MutableList<TopModel>>>

    /**
     * 获取所有项目类型
     */
    @GET("/project/tree/json")
    fun getProjectType(): Observable<BaseStateModel<MutableList<ProjectTypeModel>>>

    /**
     * 获取所有项目
     */
    @GET("project/list/{id}/json")
    fun getProject(@Path("id") id: String, @Query("cid") cid: String): Observable<BaseStateModel<ArticleModel>>

    /**
     * 获取最新项目
     */
    @GET("/article/listproject/{id}/json")
    fun getNewProject(@Path("id") id: String): Observable<BaseStateModel<NewProjectModel>>

    /**
     * 获取所有体系类型
     */
    @GET("tree/json")
    fun getAllTrees(): Observable<TreeModel>

    /**
     * 获取体系列表
     */
    @GET("article/list/{id}/json?cid={cid}")
    fun getTree(@Path("id") id: String, @Path("cid") cid: String)

    /**
     * 收藏一篇文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: String): Observable<SuccessModel>

    /**
     * 取消收藏一篇文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun uncollectArticle(@Path("id") id: String): Observable<SuccessModel>

    /**
     * 获取所有收藏文章
     */
    @GET("lg/collect/list/{id}/json")
    fun getCollects(@Path("id") id: String): Observable<BaseStateModel<CollectModel>>

    @GET("navi/json")
    fun getNavi(): Observable<BaseStateModel<MutableList<NaviModel>>>
}