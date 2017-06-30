package com.zxcx.shitang.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class UserInfoPresenter extends BasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {

    private final UserInfoModel mModel;

    public UserInfoPresenter(@NonNull UserInfoContract.View view) {
        attachView(view);
        mModel = new UserInfoModel(this);
    }

    @Override
    public void getDataSuccess(UserInfoBean bean) {
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

