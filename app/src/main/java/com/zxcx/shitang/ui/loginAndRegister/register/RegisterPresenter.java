package com.zxcx.shitang.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private final RegisterModel mModel;

    public RegisterPresenter(@NonNull RegisterContract.View view) {
        attachView(view);
        mModel = new RegisterModel(this);
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        mModel.phoneRegister(phone,code,password,appType,appChannel,appVersion);
    }

    @Override
    public void getDataSuccess(RegisterBean bean) {
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

