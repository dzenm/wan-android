package com.din.wanandroid.api

import android.content.Context
import android.widget.Toast
import com.din.wanandroid.model.SuccessModel
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object CollectHelper {

    fun collect(context: Context, id: Int) {
        Api.getRetrofit()
            .create(CollectApi::class.java)
            .collectArticle(id.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SuccessModel> {
                override fun onError(e: Throwable?) {
                    Toast.makeText(context, "收藏失败: " + e, Toast.LENGTH_SHORT).show()
                }

                override fun onNext(t: SuccessModel?) {
                    if (t?.errorCode == 0) {
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "收藏失败: " + t?.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCompleted() {
                }
            })
    }

    fun uncollect(context: Context, id: Int) {
        Api.getRetrofit()
            .create(CollectApi::class.java)
            .uncollectArticle(id.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SuccessModel> {
                override fun onError(e: Throwable?) {
                    Toast.makeText(context, "取消收藏失败: " + e, Toast.LENGTH_SHORT).show()
                }

                override fun onNext(t: SuccessModel?) {
                    if (t?.errorCode == 0) {
                        Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "取消收藏失败: " + t?.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCompleted() {
                }
            })
    }
}