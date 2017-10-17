package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

public class UserSafetyPresenter extends BasePresenter<UserSafetyContract.View> implements UserSafetyContract.Presenter {

    private final UserSafetyModel mModel;

    public UserSafetyPresenter(@NonNull UserSafetyContract.View view) {
        attachView(view);
        mModel = new UserSafetyModel(this);
    }

    public void channelBinding(int channelType, int type, String openId){
        mModel.channelBinding(channelType,type,openId);
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

    @Override
    public void postSuccess(UserInfoBean bean) {
        mView.postSuccess(bean);
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

