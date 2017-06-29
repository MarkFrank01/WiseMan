package com.zxcx.shitang.ui.classify;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class ClassifyPresenter extends BasePresenter<ClassifyContract.View> implements ClassifyContract.Presenter {

    private final ClassifyModel mModel;

    public ClassifyPresenter(@NonNull ClassifyContract.View view) {
        attachView(view);
        mModel = new ClassifyModel(this);
    }

    @Override
    public void getDataSuccess(ClassifyBean bean) {
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

