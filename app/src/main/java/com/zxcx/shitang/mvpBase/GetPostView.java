package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface GetPostView<T> extends MvpView<T>{

    void postSuccess();

    void postFail(String msg);

}
