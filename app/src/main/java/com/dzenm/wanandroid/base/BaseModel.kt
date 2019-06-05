package com.dzenm.wanandroid.base

/**
 * @author dinzhenyan
 * @date   2019-05-05 22:47
 */
data class BaseModel<T : Any>(var data: T, var errorCode: Int, var errorMsg: String)