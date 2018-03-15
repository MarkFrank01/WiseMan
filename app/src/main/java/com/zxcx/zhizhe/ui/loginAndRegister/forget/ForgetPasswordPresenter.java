package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View> implements ForgetPasswordContract.Presenter {

    private final ForgetPasswordModel mModel;

    public ForgetPasswordPresenter(@NonNull ForgetPasswordContract.View view) {
        attachView(view);
        mModel = new ForgetPasswordModel(this);
    }

    public void forgetPassword(String phone, String verifyKey, String jpushRID, String password, int appType){
        mModel.forgetPassword(phone,verifyKey,jpushRID,password,appType);
    }

    public void channelRegister(int channelType, String openId, String password, String userIcon, String name, Integer sex,
                                String birthday, String phone, String verifyKey, String jpushRID, int appType, String appChannel, String appVersion){
        mModel.channelRegister(channelType,openId,password,userIcon,name, sex,birthday,phone,verifyKey,jpushRID,appType,appChannel,appVersion);
    }

    public void changePhone(String phone, String verifyKey) {
        mModel.changePhone(phone, verifyKey);
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

