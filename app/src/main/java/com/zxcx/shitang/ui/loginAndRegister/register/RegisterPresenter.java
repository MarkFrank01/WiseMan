package com.zxcx.shitang.ui.loginAndRegister.register;

import com.zxcx.shitang.ui.loginAndRegister.register.RegisterContract;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private final RegisterModel mModel;

    public RegisterPresenter(@NonNull RegisterContract.View view) {
        attachView(view);
        mModel = new RegisterModel(this);
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

