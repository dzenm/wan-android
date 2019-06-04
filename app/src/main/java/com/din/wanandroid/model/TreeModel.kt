package com.din.wanandroid.model

data class TreeModel(
    var children: MutableList<Children>,
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int
) {
    data class Children(
        var children: MutableList<Children>,
        var courseId: Int,
        var id: Int,
        var name: String,
        var order: Int,
        var parentChapterId: Int,
        var userControlSetTop: Boolean,
        var visible: Int
    )
}