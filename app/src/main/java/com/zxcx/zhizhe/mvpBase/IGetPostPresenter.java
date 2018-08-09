package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 * 同时具有Get和Post功能的Presenter接口
 */
public interface IGetPostPresenter<T, P> extends IGetPresenter<T>, IPostPresenter<P> {


}
