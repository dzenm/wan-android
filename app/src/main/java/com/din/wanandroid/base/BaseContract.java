package com.din.wanandroid.base;

import rx.Observable;

public interface BaseContract {

    interface Model<T> extends BaseModel {

        Observable<T> getData(String url);

    }

    interface View<T> extends BaseView {

        void onSucceed(T param, String TAG);

        void onFailure(String param, String TAG);

    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void request(String url, String TAG);
    }
}