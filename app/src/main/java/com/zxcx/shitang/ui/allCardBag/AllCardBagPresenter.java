package com.zxcx.shitang.ui.allCardBag;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class AllCardBagPresenter extends BasePresenter<AllCardBagContract.View> implements AllCardBagContract.Presenter {

    private final AllCardBagModel mModel;

    public AllCardBagPresenter(@NonNull AllCardBagContract.View view) {
        attachView(view);
        mModel = new AllCardBagModel(this);
    }

    @Override
    public void getDataSuccess(AllCardBagBean bean) {
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

