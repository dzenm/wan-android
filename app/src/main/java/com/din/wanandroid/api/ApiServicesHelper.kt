package com.din.wanandroid.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.din.wanandroid.base.BaseModel
import com.dzenm.helper.dialog.PromptDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author dinzhenyan
 * @date   2019-05-28 19:22
 */
class ApiServicesHelper<T : Any>(var activity: AppCompatActivity) {

    private val TAG = ApiServicesHelper::class.java.simpleName

    /*
     * 请求的提示框
     */
    private lateinit var mPromptDialog: PromptDialog

    /*
     * 请求的回掉
     */
    private lateinit var onCallback: OnCallback<T>

    fun setOnCallback(onCallback: OnCallback<T>): ApiServicesHelper<T> {
        this.onCallback = onCallback
        return this
    }

    /**
     * 带提示框的请求
     */
    fun request(observable: Observable<BaseModel<T>>): ApiServicesHelper<T> {
        request(observable, false)
        return this;
    }

    /**
     * 自行设置是否需要带提示框的请求
     */
    fun request(observable: Observable<BaseModel<T>>, showDialog: Boolean): ApiServicesHelper<T> {
        mPromptDialog = PromptDialog.newInstance(activity)
        if (showDialog) {
            // 是否显示加载的提示框
            mPromptDialog.showLoadingPoint().setTranslucent(true)
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseModel<T>> {
                // 请求完成的回掉
                override fun onComplete() {
                    Log.d(TAG, activity::class.java.simpleName + " --- 请求完成: ")
                    dismiss()
                    onCallback?.onComplete()
                }

                // 取消订阅
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, activity::class.java.simpleName + " --- 取消订阅: ")
                    dismiss()
                    onCallback?.onCancel(d)
                    if (d.isDisposed()) {
                        d.dispose()
                    }
                }

                // 请求成功的回掉
                override fun onNext(t: BaseModel<T>) {
                    dismiss()
                    if (t?.errorCode == 0) {
                        Log.d(TAG, activity::class.java.simpleName + " --- 请求成功: ")
                        onCallback.let { it.onNext(t.data) }
                    } else {
                        Log.e(TAG, activity::class.java.simpleName + " --- 服务端返回错误: " + t?.errorCode)
                        onCallback.let { it.onFailed(t?.errorMsg) }
                    }
                }

                // 错误请求的回掉
                override fun onError(e: Throwable) {
                    Log.e(TAG, activity::class.java.simpleName + " --- 请求错误: " + e)
                    dismiss()
                    onCallback.let {
                        it.onError(e)
                        onComplete()
                    }
                }
            })
        return this;
    }

    /*
     * 取消提示框
     */
    private fun dismiss() {
        if (mPromptDialog.isAdded) {
            mPromptDialog.dismiss()
        }
    }

    abstract class OnCallback<T : Any> {

        abstract fun onNext(data: T)

        /**
         * 当请求码 = 200, 但是后台返回自己的错误
         *
         * @param s
         * @return 是否拦截Toast, true 则拦截失败Toast, false不拦截
         */
        open fun onFailed(s: String?): Boolean {
            return false
        }

        open fun onError(e: Throwable?) {

        }

        open fun onCancel(d: Disposable) {

        }

        open fun onComplete() {

        }
    }
}