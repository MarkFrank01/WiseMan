package com.zxcx.zhizhe.mvpBase;


import android.os.Bundle;
import android.view.View;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * 封装了下拉刷新功能的MVPFragment
 * @param <P>
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
