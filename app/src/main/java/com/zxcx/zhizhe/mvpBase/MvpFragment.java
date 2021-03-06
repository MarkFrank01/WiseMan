package com.zxcx.zhizhe.mvpBase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * MVP封装的Fragment
 * A simple {@link Fragment} subclass.
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
	
	protected P mPresenter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		
		return null;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter = createPresenter();
	}
	
	protected abstract P createPresenter();
	
	
	@Override
	public void onDestroyView() {
		if (mPresenter != null) {
			mPresenter.detachView();
		}
		super.onDestroyView();
	}
}
