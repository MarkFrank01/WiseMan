package com.zxcx.zhizhe.mvpBase;

import android.os.Bundle;

/**
 * MVP封装的Activity
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
	
	protected P mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPresenter = createPresenter();
		super.onCreate(savedInstanceState);
	}
	
	protected abstract P createPresenter();
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.detachView();
		}
	}
	
	
}
