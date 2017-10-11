package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

public class ChannelRegisterPresenter extends BasePresenter<ChannelRegisterContract.View> implements ChannelRegisterContract.Presenter {

    private final ChannelRegisterModel mModel;

    public ChannelRegisterPresenter(@NonNull ChannelRegisterContract.View view) {
        attachView(view);
        mModel = new ChannelRegisterModel(this);
    }

    public void channelRegister(int channelType, String openId, String userIcon, String name, Integer sex,
                                String birthday, String phone, String code, int appType, String appChannel, String appVersion){
        mModel.channelRegister(channelType,openId,userIcon,name, sex,birthday,phone,code,appType,appChannel,appVersion);
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

