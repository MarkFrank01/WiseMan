package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 * 有带返回值Post功能的Presenter接口
 */
public interface IPostPresenter<P> extends IBasePresenter {
	
	void postSuccess(P bean);
	
	void postFail(String msg);
	
}
