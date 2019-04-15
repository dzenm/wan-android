package com.din.wanandroid.api

import com.din.wanandroid.model.ProjectModel
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface ProjectApi {

    @GET("/project/tree/json")
    fun getAllProjects(): Observable<ProjectModel>

    @GET("project/list/{id}/json?cid={cid}")
    fun getProject(@Path("id") id: String, @Path("cid") cid: String)
}