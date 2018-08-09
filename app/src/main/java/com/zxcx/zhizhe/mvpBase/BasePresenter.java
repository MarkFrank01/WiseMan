package com.zxcx.zhizhe.mvpBase;

/**
 * Present基类
 */

public class BasePresenter<V> implements Presenter<V> {
	
	public V mView;
	
	@Override
	public void attachView(V mvpView) {
		this.mView = mvpView;
	}
	
	@Override
	public void detachView() {
		this.mView = null;
	}
}
