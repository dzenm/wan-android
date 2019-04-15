package com.din.wanandroid.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    companion object {
        val BASE_URL = "https://www.wanandroid.com/"
    }

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<String>

    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") username: String, @Field("password") password: String, @Field("repassword") repassword: String): Call<String>

    @GET("user/logout/json")
    fun logout(): Call<String>
}