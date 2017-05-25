package com.zxcx.shitang.mvpBase;

/**
 * Created by WuXiaolong on 2016/3/30.
 */
public interface Presenter<V> {

    void attachView(V mvpView);

//    void attachView(M mvpModel, V mvpView);

    void detachView();

}