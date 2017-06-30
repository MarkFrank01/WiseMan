package com.zxcx.shitang.ui.card.cardBag;

import com.zxcx.shitang.ui.card.cardBag.CardBagContract;
import com.zxcx.shitang.ui.card.cardBag.CardBagModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class CardBagPresenter extends BasePresenter<CardBagContract.View> implements CardBagContract.Presenter {

    private final CardBagModel mModel;

    public CardBagPresenter(@NonNull CardBagContract.View view) {
        attachView(view);
        mModel = new CardBagModel(this);
    }

    @Override
    public void getDataSuccess(CardBagBean bean) {
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

