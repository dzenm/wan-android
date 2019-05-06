package com.din.wanandroid.model

data class NaviModel(
    var articles: MutableList<Articles>, var cid: Int, var name: String
) {
    data class Articles(
        var apkLink: String, var author: String, var chapterId: Int,
        var chapterName: String, var collect: Boolean, var courseId: Int,
        var desc: String, var envelopePic: String, var fresh: Boolean,
        var id: Int, var link: String, var niceDate: String,
        var origin: String, var prefix: String, var projectLink: String,
        var publishTime: Long, var superChapterId: Int, var superChapterName: String,
        var tags: MutableList<Tags>, var title: String, var type: Int,
        var userId: Int, var visible: Int, var zan: Int
    ) {
        data class Tags(
            var name: String, var url: String
        )
    }
}