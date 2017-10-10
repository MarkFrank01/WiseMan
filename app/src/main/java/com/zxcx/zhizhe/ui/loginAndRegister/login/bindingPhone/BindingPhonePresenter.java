package com.zxcx.zhizhe.ui.loginAndRegister.login.bindingPhone;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class BindingPhonePresenter extends BasePresenter<BindingPhoneContract.View> implements BindingPhoneContract.Presenter {

    private final BindingPhoneModel mModel;

    public BindingPhonePresenter(@NonNull BindingPhoneContract.View view) {
        attachView(view);
        mModel = new BindingPhoneModel(this);
    }

    @Override
    public void getDataSuccess(BindingPhoneBean bean) {
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

