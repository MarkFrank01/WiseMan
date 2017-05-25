package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface BaseView {

    void showProgress();

    void hideProgress();

    void setText(String text);

    void toastFail(String msg);

}
