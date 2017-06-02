package com.zxcx.shitang.ui.collect.cardCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class CardCollectPresenter extends BasePresenter<CardCollectContract.View> implements CardCollectContract.Presenter {

    private final CardCollectModel mModel;

    public CardCollectPresenter(@NonNull CardCollectContract.View view) {
        attachView(view);
        mModel = new CardCollectModel(this);
    }

    @Override
    public void getDataSuccess(CardCollectBean bean) {
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

