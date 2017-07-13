package com.zxcx.shitang.ui.my.feedback;

import com.zxcx.shitang.ui.my.feedback.FeedbackContract;
import com.zxcx.shitang.ui.my.feedback.FeedbackModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class FeedbackPresenter extends BasePresenter<FeedbackContract.View> implements FeedbackContract.Presenter {

    private final FeedbackModel mModel;

    public FeedbackPresenter(@NonNull FeedbackContract.View view) {
        attachView(view);
        mModel = new FeedbackModel(this);
    }

    @Override
    public void getDataSuccess(FeedbackBean bean) {
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

