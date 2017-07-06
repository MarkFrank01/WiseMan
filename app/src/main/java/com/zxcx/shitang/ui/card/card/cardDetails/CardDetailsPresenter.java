package com.zxcx.shitang.ui.card.card.cardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class CardDetailsPresenter extends BasePresenter<CardDetailsContract.View> implements CardDetailsContract.Presenter {

    private final CardDetailsModel mModel;

    public CardDetailsPresenter(@NonNull CardDetailsContract.View view) {
        attachView(view);
        mModel = new CardDetailsModel(this);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {
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

