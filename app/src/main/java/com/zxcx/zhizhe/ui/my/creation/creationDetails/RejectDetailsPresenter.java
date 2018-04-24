package com.zxcx.zhizhe.ui.my.creation.creationDetails;

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

    public void getReviewDetails(int RejectId){
        mModel.getReviewDetails(RejectId);
    }

    public void getDraftDetails(int RejectId){
        mModel.getDraftDetails(RejectId);
    }

    public void submitReview(int noteId) {
        mModel.submitReview(noteId);
    }

    public void deleteCard(int cardId) {
        mModel.deleteCard(cardId);
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

    @Override
    public void postSuccess() {
        mView.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }
}

