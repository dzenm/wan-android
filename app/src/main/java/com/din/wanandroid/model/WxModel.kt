package com.din.wanandroid.model

/**
 * @author dinzhenyan
 * @date   2019-04-25 23:21
 */
data class WxModel(
    val children: MutableList<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Long,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)