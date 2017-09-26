package com.zxcx.shitang.ui.my.feedback.feedback;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

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

    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail(String msg) {

    }
}

