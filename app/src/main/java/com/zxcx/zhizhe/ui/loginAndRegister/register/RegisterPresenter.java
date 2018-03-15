package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private final RegisterModel mModel;

    public RegisterPresenter(@NonNull RegisterContract.View view) {
        attachView(view);
        mModel = new RegisterModel(this);
    }

    public void phoneRegister(String phone, String verifyKey, String jpushRID, String password, int appType, String appChannel, String appVersion){
        mModel.phoneRegister(phone,verifyKey,jpushRID,password,appType,appChannel,appVersion);
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

