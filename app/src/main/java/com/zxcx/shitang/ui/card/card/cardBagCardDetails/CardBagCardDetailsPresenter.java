package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class CardBagCardDetailsPresenter extends BasePresenter<CardBagCardDetailsContract.View> implements CardBagCardDetailsContract.Presenter {

    private final CardBagCardDetailsModel mModel;

    public CardBagCardDetailsPresenter(@NonNull CardBagCardDetailsContract.View view) {
        attachView(view);
        mModel = new CardBagCardDetailsModel(this);
    }

    @Override
    public void getDataSuccess(CardBagCardDetailsBean bean) {
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

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

