package com.zxcx.shitang.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class HotPresenter extends BasePresenter<HotContract.View> implements HotContract.Presenter {

    private final HotModel mModel;

    public HotPresenter(@NonNull HotContract.View view) {
        attachView(view);
        mModel = new HotModel(this);
    }

    @Override
    public void getDataSuccess(HotBean bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    @Override
    public void showLoading() {
        mView.showLoading();
    }

    @Override
    public void hideLoading() {
        mView.hideLoading();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

