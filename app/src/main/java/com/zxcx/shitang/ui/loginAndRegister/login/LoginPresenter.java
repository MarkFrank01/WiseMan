package com.zxcx.shitang.ui.loginAndRegister.login;

import com.zxcx.shitang.ui.loginAndRegister.login.LoginContract;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final LoginModel mModel;

    public LoginPresenter(@NonNull LoginContract.View view) {
        attachView(view);
        mModel = new LoginModel(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

