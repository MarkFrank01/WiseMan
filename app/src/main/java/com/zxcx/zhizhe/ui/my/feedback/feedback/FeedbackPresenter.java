package com.zxcx.zhizhe.ui.my.feedback.feedback;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class FeedbackPresenter extends BasePresenter<FeedbackContract.View> implements FeedbackContract.Presenter {

    private final FeedbackModel mModel;

    public FeedbackPresenter(@NonNull FeedbackContract.View view) {
        attachView(view);
        mModel = new FeedbackModel(this);
    }

    public void feedback(String content, String contact,int appType, String appChannel, String appVersion){
        mModel.feedback(content,contact,appType,appChannel,appVersion);
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

