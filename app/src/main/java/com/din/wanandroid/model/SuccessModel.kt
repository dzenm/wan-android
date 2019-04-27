package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel

data class SuccessModel(var data: String, var errorCode: Int, var errorMsg: String) : BaseModel