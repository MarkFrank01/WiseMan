package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface IGetPresenter<T> extends IBasePresenter {

    void getDataSuccess(T bean);

    void getDataFail(String msg);

}
