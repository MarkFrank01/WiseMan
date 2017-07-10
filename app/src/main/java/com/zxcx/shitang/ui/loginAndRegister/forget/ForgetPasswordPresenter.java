package com.zxcx.shitang.ui.loginAndRegister.forget;

import com.zxcx.shitang.ui.loginAndRegister.forget.ForgetPasswordContract;
import com.zxcx.shitang.ui.loginAndRegister.forget.ForgetPasswordModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View> implements ForgetPasswordContract.Presenter {

    private final ForgetPasswordModel mModel;

    public ForgetPasswordPresenter(@NonNull ForgetPasswordContract.View view) {
        attachView(view);
        mModel = new ForgetPasswordModel(this);
    }

    @Override
    public void getDataSuccess(ForgetPasswordBean bean) {
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

