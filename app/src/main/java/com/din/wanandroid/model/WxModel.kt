package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel

/**
 * @author dinzhenyan
 * @date   2019-04-25 23:21
 * @IDE    Android Studio
 */
data class WxModel(
    val data: MutableList<Data>,
    val errorCode: Int,
    val errorMsg: String
) : BaseModel {
    data class Data(
        val children: MutableList<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    )
}
