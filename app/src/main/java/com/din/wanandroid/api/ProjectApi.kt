package com.din.wanandroid.api

import com.din.wanandroid.model.ArticleModel
import com.din.wanandroid.model.NewProjectModel
import com.din.wanandroid.model.ProjectTypeModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface ProjectApi {

    @GET("/project/tree/json")
    fun getProjectType(): Observable<ProjectTypeModel>

    @GET("project/list/{id}/json")
    fun getProject(@Path("id") id: String, @Query("cid") cid: String): Observable<ArticleModel>

    @GET("/article/listproject/{id}/json")
    fun getNewProject(@Path("id") id: String): Observable<NewProjectModel>

}