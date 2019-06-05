package com.dzenm.wanandroid.api

import com.dzenm.wanandroid.base.BaseModel
import com.dzenm.wanandroid.model.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * @author dinzhenyan
 * @date   2019-05-05 22:45
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
    fun getBanner(): Observable<BaseModel<MutableList<BannerModel>>>

    /**
     * 获取所有文章
     */
    @GET("article/list/{id}/json")
    fun getArticle(@Path("id") id: Int): Observable<BaseModel<ArticleModel>>

    /**
     * 获取置顶文章
     */
    @GET("article/top/json")
    fun getTop(): Observable<BaseModel<MutableList<TopModel>>>

    /**
     * 获取所有项目类型
     */
    @GET("project/tree/json")
    fun getProjectType(): Observable<BaseModel<MutableList<ProjectTypeModel>>>

    /**
     * 获取所有项目
     */
    @GET("project/list/{id}/json")
    fun getProject(@Path("id") id: Int, @Query("cid") cid: Int): Observable<BaseModel<ArticleModel>>

    /**
     * 获取最新项目
     */
    @GET("article/listproject/{id}/json")
    fun getNewProject(@Path("id") id: Int): Observable<BaseModel<NewProjectModel>>

    /**
     * 获取微信公众号的数据
     */
    @GET("wxarticle/chapters/json")
    fun getWx(): Observable<BaseModel<MutableList<WxModel>>>

    /**
     * 获取微信公众号历史数据
     */
    @GET("wxarticle/list/{chapterId}/{curPage}/json")
    fun getWxArticle(@Path("chapterId") chapterId: Int, @Path("curPage") curPage: Int): Observable<BaseModel<WxArticleModel>>

    /**
     * 获取所有体系类型
     */
    @GET("tree/json")
    fun getTree(): Observable<BaseModel<MutableList<TreeModel>>>

    /**
     * 获取体系列表
     */
    @GET("article/list/{page}/json")
    fun getTreeArticle(@Path("page") page: Int, @Query("cid") cid: Int): Observable<BaseModel<TreeArticleModel>>

    /**
     * 收藏一篇文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): Observable<SuccessModel>

    /**
     * 取消收藏一篇文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun uncollectArticle(@Path("id") id: Int): Observable<SuccessModel>

    /**
     * 获取所有收藏文章
     */
    @GET("lg/collect/list/{id}/json")
    fun getCollects(@Path("id") id: Int): Observable<BaseModel<CollectModel>>

    @GET("navi/json")
    fun getNavi(): Observable<BaseModel<MutableList<NaviModel>>>
}