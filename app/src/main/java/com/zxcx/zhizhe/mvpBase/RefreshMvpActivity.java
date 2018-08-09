package com.zxcx.zhizhe.mvpBase;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zxcx.zhizhe.R;

/**
 * 封装了下拉刷新功能的MVPActivity
 */

public abstract class RefreshMvpActivity<P extends BasePresenter> extends MvpActivity<P> implements
	OnRefreshListener {
	
	public SmartRefreshLayout mRefreshLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mRefreshLayout = findViewById(R.id.refresh_layout);
		initRefreshLayout();
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		mRefreshLayout = findViewById(R.id.refresh_layout);
		initRefreshLayout();
	}
	
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		mRefreshLayout = findViewById(R.id.refresh_layout);
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
