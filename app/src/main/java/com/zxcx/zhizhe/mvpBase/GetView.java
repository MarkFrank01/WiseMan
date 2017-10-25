package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface GetView<T> extends BaseView {

    void getDataSuccess(T bean);

    void toastFail(String msg);

}
