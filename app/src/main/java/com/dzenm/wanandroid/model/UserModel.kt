package com.dzenm.wanandroid.model

import java.io.Serializable

data class UserModel(
    var username: String, var type: Int, var token: String,
    var password: String, var id: Int, var icon: String,
    var email: String
) : Serializable

