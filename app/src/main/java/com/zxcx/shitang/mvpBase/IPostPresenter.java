package com.zxcx.shitang.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface IPostPresenter<P>{

    void postSuccess(P bean);

    void postFail(String msg);

}
