package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final LoginModel mModel;

    public LoginPresenter(@NonNull LoginContract.View view) {
        attachView(view);
        mModel = new LoginModel(this);
    }

    public void phoneLogin(String phone, String password, int appType, String appChannel, String appVersion){
        mModel.phoneLogin(phone,password,appType,appChannel,appVersion);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
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

