package com.din.wanandroid.api

import com.din.wanandroid.model.TreeModel
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface TreeApi {

    @GET("tree/json")
    fun getAllTrees(): Observable<TreeModel>

    @GET("article/list/{id}/json?cid={cid}")
    fun getTree(@Path("id") id: String, @Path("cid") cid: String)
}