package com.zxcx.shitang.ui.collect.cardBagCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class CardBagCollectPresenter extends BasePresenter<CardBagCollectContract.View> implements CardBagCollectContract.Presenter {

    private final CardBagCollectModel mModel;

    public CardBagCollectPresenter(@NonNull CardBagCollectContract.View view) {
        attachView(view);
        mModel = new CardBagCollectModel(this);
    }

    @Override
    public void getDataSuccess(CardBagCollectBean bean) {
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

