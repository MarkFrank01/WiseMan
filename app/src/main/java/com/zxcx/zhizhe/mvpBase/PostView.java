package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 */
public interface PostView<P> extends BaseView {
	
	void postSuccess(P bean);
	
	void postFail(String msg);
	
}
