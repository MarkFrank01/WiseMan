package com.zxcx.zhizhe.ui.my.creation.rejectDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class RejectDetailsPresenter extends BasePresenter<RejectDetailsContract.View> implements RejectDetailsContract.Presenter {

    private final RejectDetailsModel mModel;

    public RejectDetailsPresenter(@NonNull RejectDetailsContract.View view) {
        attachView(view);
        mModel = new RejectDetailsModel(this);
    }

    public void getRejectDetails(int RejectId){
        mModel.getRejectDetails(RejectId);
    }

    @Override
    public void getDataSuccess(RejectDetailsBean bean) {
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

