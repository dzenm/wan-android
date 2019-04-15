package com.din.wanandroid.util

import android.content.Context
import android.text.TextUtils
import androidx.core.content.edit
import com.din.wanandroid.model.UserModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

const val Sharef = "shared_pref_user"
const val Sharef_Login = "shared_pref_login"

const val User_Key = "user"
const val Login_Key = "login"

/*
 * 写入用户数据
 */
fun writeUser(context: Context, userModels: ArrayList<UserModel>) {
    val sharePreferences = context.getSharedPreferences(Sharef, Context.MODE_PRIVATE)
    val json = Gson().toJson(userModels)
    sharePreferences.edit {
        putString(User_Key, json)
        commit()
    }
}

/*
 * 读取用户数据
 */
fun readUser(context: Context): ArrayList<UserModel>? {
    val sharedPreferences = context.getSharedPreferences(Sharef, Context.MODE_PRIVATE)
    val json = sharedPreferences?.getString(User_Key, "")
    if (TextUtils.isEmpty(json)) {
        return null
    }
    val userModels = Gson().fromJson<ArrayList<UserModel>>(json, object : TypeToken<ArrayList<UserModel>>() {}.type)
    return userModels
}

/*
 * 获取登录状态
 */
fun getLoginState(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(Sharef_Login, Context.MODE_PRIVATE)
    val login = sharedPreferences.getBoolean(Login_Key, false)
    return login
}

/*
 * 设置登录状态
 */
fun setLoginState(context: Context, login: Boolean) {
    val sharedPreferences = context.getSharedPreferences(Sharef_Login, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(Login_Key, login)
        commit()
    }
}

/*
 * 删除登录记录文件
 */
fun deleteLoginFile(): Boolean {
    val file = File("/data/data/com.din.wanandroid/shared_prefs/${Sharef}.xml")
    if (file.exists()) {
        return file.delete()
    }
    return true
}