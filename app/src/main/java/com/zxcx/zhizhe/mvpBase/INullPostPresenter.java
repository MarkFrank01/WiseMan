package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface INullPostPresenter extends IBasePresenter{

    void postSuccess();

    void postFail(String msg);

}
