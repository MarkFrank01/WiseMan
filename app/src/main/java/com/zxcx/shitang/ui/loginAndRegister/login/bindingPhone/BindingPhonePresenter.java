package com.zxcx.shitang.ui.loginAndRegister.login.bindingPhone;

import com.zxcx.shitang.ui.loginAndRegister.login.bindingPhone.BindingPhoneContract;
import com.zxcx.shitang.ui.loginAndRegister.login.bindingPhone.BindingPhoneModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

