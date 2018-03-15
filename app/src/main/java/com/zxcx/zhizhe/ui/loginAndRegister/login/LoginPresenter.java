package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final LoginModel mModel;

    public LoginPresenter(@NonNull LoginContract.View view) {
        attachView(view);
        mModel = new LoginModel(this);
    }

    public void smsCodeLogin(String phone, String verifyKey, String jpushRID, int appType, String appChannel, String appVersion){
        mModel.smsCodeLogin(phone,verifyKey,jpushRID,appType,appChannel,appVersion);
    }

    public void channelLogin(int channelType, String openId, String jpushRID, int appType, String appChannel, String appVersion){
        mModel.channelLogin(channelType,openId,jpushRID,appType,appChannel,appVersion);
    }

    public void smsCodeVerification(String phone, String code){
        mModel.smsCodeVerification(phone,code);
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
    public void channelLoginSuccess(LoginBean bean) {
        mView.channelLoginSuccess(bean);
    }

    @Override
    public void channelLoginNeedRegister() {
        mView.channelLoginNeedRegister();
    }

    @Override
    public void needRegister() {
        mView.needRegister();
    }

    @Override
    public void smsCodeVerificationSuccess(SMSCodeVerificationBean bean) {
        mView.smsCodeVerificationSuccess(bean);
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

