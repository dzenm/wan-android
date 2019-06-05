package com.dzenm.wanandroid.model

/**
 * @author dinzhenyan
 * @date   2019-04-26 10:39
 */
data class NewProjectModel(
    var curPage: Int,
    var datas: MutableList<Datas>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) {
    data class Datas(
        var apkLink: String,
        var author: String,
        var chapterId: Int,
        var chapterName: String,
        var collect: Boolean,
        var courseId: Int,
        var desc: String,
        var envelopePic: String,
        var fresh: Boolean,
        var id: Int,
        var link: String,
        var niceDate: String,
        var origin: String,
        var prefix: String,
        var projectLink: String,
        var publishTime: Long,
        var superChapterId: Int,
        var superChapterName: String,
        var tags: MutableList<Tag>,
        var title: String,
        var type: Int,
        var userId: Int,
        var visible: Int,
        var zan: Int
    ) {

        data class Tag(
            var name: String,
            var url: String
        )
    }
}
