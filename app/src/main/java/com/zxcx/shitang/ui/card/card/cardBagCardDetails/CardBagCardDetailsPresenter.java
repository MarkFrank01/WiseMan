package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import com.zxcx.shitang.ui.card.card.cardBagCardDetails.CardBagCardDetailsContract;
import com.zxcx.shitang.ui.card.card.cardBagCardDetails.CardBagCardDetailsModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

