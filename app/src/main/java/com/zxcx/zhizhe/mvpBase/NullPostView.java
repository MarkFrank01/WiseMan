package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface NullPostView extends BaseView{

    void postSuccess();

    void postFail(String msg);

}
