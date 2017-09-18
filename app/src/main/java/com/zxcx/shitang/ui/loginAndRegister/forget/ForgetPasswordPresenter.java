package com.zxcx.shitang.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View> implements ForgetPasswordContract.Presenter {

    private final ForgetPasswordModel mModel;

    public ForgetPasswordPresenter(@NonNull ForgetPasswordContract.View view) {
        attachView(view);
        mModel = new ForgetPasswordModel(this);
    }

    public void changePassword(String phone, String code, String password, int appType){
        mModel.changePassword(phone,code,password,appType);
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

    }

    @Override
    public void postFail(String msg) {

    }
}

