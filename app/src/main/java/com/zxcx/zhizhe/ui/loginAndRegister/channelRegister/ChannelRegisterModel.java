package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;


public class ChannelRegisterModel extends BaseModel<ChannelRegisterContract.Presenter> {

    public ChannelRegisterModel(@NonNull ChannelRegisterContract.Presenter present) {
        this.mPresent = present;
    }

    public void channelRegister(int channelType, String openId, String userIcon, String name, Integer sex,
                              String birthday, String phone, String code, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().channelRegister(channelType,openId,userIcon,name,
                sex,birthday,phone,code,appType,appChannel,appVersion)
                .compose(this.<BaseBean<LoginBean>>io_main())
                .compose(this.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresent) {
                    @Override
                    public void onNext(LoginBean bean) {
                        mPresent.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

}


