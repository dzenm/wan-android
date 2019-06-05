package com.dzenm.wanandroid.model

/**
 * @author dinzhenyan
 * @date   2019-06-02 10:51
 */
data class WxArticleModel(
    val curPage: Int,
    val datas: MutableList<Datas>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
) {
    data class Datas(
        val apkLink: String,
        val author: String,
        val chapterId: Int,
        val chapterName: String,
        var collect: Boolean,
        val courseId: Int,
        val desc: String,
        val envelopePic: String,
        val fresh: Boolean,
        val id: Int,
        val link: String,
        val niceDate: String,
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: MutableList<Tag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        var zan: Int
    ) {
        data class Tag(
            val name: String,
            val url: String
        )
    }
}
