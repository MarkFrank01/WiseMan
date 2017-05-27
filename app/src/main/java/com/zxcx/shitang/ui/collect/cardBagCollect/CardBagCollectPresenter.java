package com.zxcx.shitang.ui.collect.cardBagCollect;

import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

