package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel
import java.io.Serializable

data class UserModel(
    var username: String, var type: Int, var token: String,
    var password: String, var id: Int, var icon: String,
    var email: String
) : Serializable, BaseModel

