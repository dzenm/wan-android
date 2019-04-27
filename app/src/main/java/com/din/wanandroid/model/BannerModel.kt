package com.din.wanandroid.model

import com.din.wanandroid.base.BaseModel

data class BannerModel(
    var data: MutableList<Data>,
    var errorCode: Int,
    var errorMsg: String
) : BaseModel {
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
