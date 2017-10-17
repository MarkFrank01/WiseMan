package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;


public class ChannelRegisterModel extends BaseModel<ChannelRegisterContract.Presenter> {

    public ChannelRegisterModel(@NonNull ChannelRegisterContract.Presenter present) {
        this.mPresenter = present;
    }

    public void channelRegister(int channelType, String openId, String userIcon, String name, Integer sex,
                              String birthday, String phone, String code, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().channelRegister(channelType,openId,userIcon,name,
                sex,birthday,phone,code,appType,appChannel,appVersion)
                .compose(BaseRxJava.<LoginBean>handleResult())
                .compose(BaseRxJava.<LoginBean>io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

}


