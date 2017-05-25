package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface IGetPostPresenter<T> {

    void getDataSuccess(T bean);

    void getDataFail(String msg);

    void postSuccess();

    void postFail(String msg);

}
