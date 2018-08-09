package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 * 具有Get功能的Presenter接口
 */
public interface IGetPresenter<T> extends IBasePresenter {
	
	void getDataSuccess(T bean);
	
	void getDataFail(String msg);
	
}
