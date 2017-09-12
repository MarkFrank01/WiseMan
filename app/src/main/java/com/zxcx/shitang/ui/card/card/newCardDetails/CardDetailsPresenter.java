package com.zxcx.shitang.ui.card.card.newCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;
import com.zxcx.shitang.mvpBase.PostBean;

public class CardDetailsPresenter extends BasePresenter<CardDetailsContract.View> implements CardDetailsContract.Presenter {

    private final CardDetailsModel mModel;

    public CardDetailsPresenter(@NonNull CardDetailsContract.View view) {
        attachView(view);
        mModel = new CardDetailsModel(this);
    }

    public void getCardDetails(int userId, int cardId){
        mModel.getCardDetails(userId, cardId);
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

    @Override
    public void postSuccess(PostBean bean) {

    }

    @Override
    public void postFail(String msg) {

    }
}

