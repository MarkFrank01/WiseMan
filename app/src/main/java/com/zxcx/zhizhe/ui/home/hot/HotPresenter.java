package com.zxcx.zhizhe.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

import java.util.List;

public class HotPresenter extends BasePresenter<HotContract.View> implements HotContract.Presenter {

    private final HotModel mModel;

    public HotPresenter(@NonNull HotContract.View view) {
        attachView(view);
        mModel = new HotModel(this);
    }

    public void getHotCard(int page){
        mModel.getHotCard(page);
    }

    @Override
    public void getDataSuccess(List<HotBean> s) {
        mView.getDataSuccess(s);
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

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

