package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public class CardBagCardDetailsPresenter extends BasePresenter<CardBagCardDetailsContract.View> implements CardBagCardDetailsContract.Presenter {

    private final CardBagCardDetailsModel mModel;

    public CardBagCardDetailsPresenter(@NonNull CardBagCardDetailsContract.View view) {
        attachView(view);
        mModel = new CardBagCardDetailsModel(this);
    }

    public void getAllCardId(int cardBagId) {
        mModel.getAllCardId(cardBagId);
    }

    public void getCardDetails(int cardId){
        mModel.getCardDetails(cardId);
    }

    public void likeCard(int cardId){
        mModel.likeCard(cardId);
    }

    public void unLikeCard(int cardId){
        mModel.unLikeCard(cardId);
    }

    public void removeCollectCard(int cardId){
        mModel.removeCollectCard(cardId);
    }

    @Override
    public void getAllCardIdSuccess(List<CardBagCardDetailsBean> list) {
        mView.getAllCardIdSuccess(list);
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

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }

    @Override
    public void postSuccess() {
        mView.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }
}

