package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel

/**
 * @author dinzhenyan
 * @date   2019-04-25 16:09
 * @IDE    Android Studio
 */
/**
 * @author dinzhenyan
 * @date   2019-04-25 16:09
 * @IDE    Android Studio
 */
data class CollectModel(
    var data: Data, var errorCode: Int, var errorMsg: String
) : BaseModel {
    data class Data(
        var curPage: Int, var datas: MutableList<Datas>, var offset: Int, var over: Boolean,
        var pageCount: Int, var size: Int, var total: Int
    ) {
        data class Datas(
            var apkLink: String, var author: String, var chapterId: Int, var chapterName: String,
            var collect: Boolean, var courseId: Int, var desc: String, var envelopePic: String,
            var fresh: Boolean, var id: Int, var link: String, var niceDate: String, var origin: String,
            var projectLink: String, var publishTime: Long, var superChapterId: Int,
            var superChapterName: String, var tags: MutableList<Tags>, var title: String, var type: Int,
            var userId: Int, var visible: Int, var zan: Int
        ) {
            data class Tags(var name: String, var url: String)
        }
    }
}