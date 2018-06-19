package com.zxcx.zhizhe.ui.card.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

import java.util.Date;
import java.util.List;

public class HotCardPresenter extends BasePresenter<HotCardContract.View> implements HotCardContract.Presenter {

    private final HotCardModel mModel;

    public HotCardPresenter(@NonNull HotCardContract.View view) {
        attachView(view);
        mModel = new HotCardModel(this);
    }

    public void getHotCard(Date lastRefresh, int page){
        mModel.getHotCard(lastRefresh,page);
    }

    @Override
    public void getDataSuccess(List<CardBean> s) {
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

