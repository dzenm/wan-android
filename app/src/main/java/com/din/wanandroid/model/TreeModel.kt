package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel

data class TreeModel(
    var data: MutableList<Data>, var errorCode: Int, var errorMsg: String
) : BaseModel {
    data class Data(
        var children: MutableList<Children>, var courseId: Int, var id: Int,
        var name: String, var order: Int, var parentChapterId: Int,
        var userControlSetTop: Boolean, var visible: Int
    ) {
        data class Children(
            var children: MutableList<Children>, var courseId: Int, var id: Int,
            var name: String, var order: Int, var parentChapterId: Int,
            var userControlSetTop: Boolean, var visible: Int
        )
    }
}