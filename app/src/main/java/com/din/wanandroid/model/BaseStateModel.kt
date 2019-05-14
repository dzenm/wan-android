package com.din.wanandroid.model

/**
 * @author dinzhenyan
 * @date   2019-05-05 22:47
 * @IDE    Android Studio
 */
data class BaseStateModel<T : Any>(var data: T, var errorCode: Int, var errorMsg: String)