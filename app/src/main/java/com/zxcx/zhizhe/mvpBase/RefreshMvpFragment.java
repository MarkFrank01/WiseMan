package com.zxcx.zhizhe.mvpBase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class RefreshMvpFragment<P extends BasePresenter> extends MvpFragment<P> implements
	OnRefreshListener {
	
	public SmartRefreshLayout mRefreshLayout;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initRefreshLayout();
	}
	
	private void initRefreshLayout() {
		if (mRefreshLayout != null) {
			mRefreshLayout.setOnRefreshListener(this);
		}
	}
	
	@Override
	public void toastFail(String msg) {
		mRefreshLayout.finishRefresh();
		super.toastFail(msg);
	}
}
