package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface MvpView<T> extends BaseView {

    void getDataSuccess(T bean);

}
