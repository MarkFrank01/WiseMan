package com.zxcx.zhizhe.mvpBase;


import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Mode基类
 */

public class BaseModel<T extends IBasePresenter> {
	
	@Nullable
	public T mPresenter;
	protected Disposable mDisposable;
	//    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
	private CompositeDisposable mCompositeSubscription = null;
	
	public void onDestroy() {
		mPresenter = null;
		if (mCompositeSubscription != null) {
			mCompositeSubscription.clear();//取消注册，以避免内存泄露
		}
	}
	
	protected void addSubscription(Disposable subscription) {
		if (mCompositeSubscription == null) {
			mCompositeSubscription = new CompositeDisposable();
		}
		mCompositeSubscription.add(subscription);
	}
}
