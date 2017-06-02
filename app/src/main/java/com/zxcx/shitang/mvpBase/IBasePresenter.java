package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface IBasePresenter<T> {

    void getDataSuccess(T bean);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

}
