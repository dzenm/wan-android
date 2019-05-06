package com.din.wanandroid.model

/**
 * @author dinzhenyan
 * @date   2019-04-26 13:44
 * @IDE    Android Studio
 */
data class ProjectTypeModel(
    val children: MutableList<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)