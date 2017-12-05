package com.zxcx.zhizhe.mvpBase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zxcx.zhizhe.widget.RefreshHeader;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class RefreshMvpFragment<P extends BasePresenter> extends MvpFragment<P> implements PtrHandler {

    public PtrFrameLayout mRefreshLayout;
    public RefreshHeader mRefreshHeader;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        if (mRefreshLayout != null) {
            mRefreshHeader = new RefreshHeader(mActivity);
            mRefreshLayout.setDurationToCloseHeader(2000);
            mRefreshLayout.setHeaderView(mRefreshHeader);
            mRefreshLayout.addPtrUIHandler(mRefreshHeader);
            mRefreshLayout.setPtrHandler(this);
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !frame.getContentView().canScrollVertically(-1);
    }
}
