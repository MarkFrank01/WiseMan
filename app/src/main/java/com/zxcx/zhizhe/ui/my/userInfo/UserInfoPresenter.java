package com.zxcx.zhizhe.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class UserInfoPresenter extends BasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {

    private final UserInfoModel mModel;

    public UserInfoPresenter(@NonNull UserInfoContract.View view) {
        attachView(view);
        mModel = new UserInfoModel(this);
    }

    public void changeImageUrl(String imageUrl){
        mModel.changeImageUrl(imageUrl);
    }

    public void changeBirth(String birth){
        mModel.changeBirth(birth);
    }

    @Override
    public void changeImageSuccess(UserInfoBean bean) {
        mView.changeImageSuccess(bean);
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

    @Override
    public void postSuccess(UserInfoBean bean) {
        mView.postSuccess(bean);
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }
}
