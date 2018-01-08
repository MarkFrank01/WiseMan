package com.zxcx.zhizhe.mvpBase;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.widget.RefreshHeader;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class RefreshMvpActivity<P extends BasePresenter> extends MvpActivity<P> implements PtrHandler{

    public PtrFrameLayout mRefreshLayout;
    public RefreshHeader mRefreshHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mRefreshLayout = (PtrFrameLayout) findViewById(R.id.refresh_layout);
        initRefreshLayout();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mRefreshLayout = (PtrFrameLayout) findViewById(R.id.refresh_layout);
        initRefreshLayout();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mRefreshLayout = (PtrFrameLayout) findViewById(R.id.refresh_layout);
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshHeader = new RefreshHeader(mActivity);
        mRefreshLayout.setDurationToClose(200);
        mRefreshLayout.setDurationToCloseHeader(1000);
        mRefreshLayout.setHeaderView(mRefreshHeader);
        mRefreshLayout.addPtrUIHandler(mRefreshHeader);
        mRefreshLayout.setPtrHandler(this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !frame.getContentView().canScrollVertically(-1);
    }

    @Override
    public void toastFail(String msg) {
        mRefreshLayout.refreshComplete();
        super.toastFail(msg);
    }
}
