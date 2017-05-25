package com.zxcx.shitang.mvpBase;

/**
 * Created by WuXiaolong on 2016/3/30.
 */
public class BasePresenter<V> implements Presenter<V> {
    public V mView;

    @Override
    public void attachView(V mvpView) {
        this.mView = mvpView;
    }


    @Override
    public void detachView() {
        this.mView = null;

    }
}
