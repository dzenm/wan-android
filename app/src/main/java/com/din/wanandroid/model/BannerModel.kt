package com.din.wanandroid.model

data class BannerModel(var data: MutableList<Data>, var errorCode: Int, var errorMsg: String) {
    data class Data(
        var desc: String,
        var id: Int,
        var imagePath: String,
        var isVisible: Int,
        var order: Int,
        var title: String,
        var type: Int,
        var url: String
    )
}
