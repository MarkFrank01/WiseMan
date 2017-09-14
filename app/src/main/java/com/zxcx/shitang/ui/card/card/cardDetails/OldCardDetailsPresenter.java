package com.zxcx.shitang.ui.card.card.cardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class OldCardDetailsPresenter extends BasePresenter<OldCardDetailsContract.View> implements OldCardDetailsContract.Presenter {

    private final OldCardDetailsModel mModel;

    public OldCardDetailsPresenter(@NonNull OldCardDetailsContract.View view) {
        attachView(view);
        mModel = new OldCardDetailsModel(this);
    }

    @Override
    public void getDataSuccess(OldCardDetailsBean bean) {
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

