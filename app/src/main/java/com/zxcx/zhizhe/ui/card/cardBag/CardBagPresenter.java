package com.zxcx.zhizhe.ui.card.cardBag;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

public class CardBagPresenter extends BasePresenter<CardBagContract.View> implements CardBagContract.Presenter {

    private final CardBagModel mModel;

    public CardBagPresenter(@NonNull CardBagContract.View view) {
        attachView(view);
        mModel = new CardBagModel(this);
    }

    public void getCardBagCardList(int id, int page, int pageSize){
        mModel.getCardBagCardList(id,page,pageSize);
    }

    @Override
    public void getDataSuccess(List<CardBean> bean) {
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

