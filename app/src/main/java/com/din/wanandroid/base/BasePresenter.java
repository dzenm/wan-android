package com.din.wanandroid.base;


public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V view = null;
    protected M model;

}