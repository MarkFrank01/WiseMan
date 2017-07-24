package com.zxcx.shitang.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;

public class LoginModel extends BaseModel<LoginContract.Presenter> {

    public LoginModel(@NonNull LoginContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneLogin(String phone, String password, String appType, String appChannel, String appVersion){
        subscription = AppClient.getAPIService().phoneLogin(phone,password,appType,appChannel,appVersion)
                .compose(this.<BaseBean<LoginBean>>io_main())
                .compose(this.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresent) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        mPresent.getDataSuccess(loginBean);
                    }
                });
        addSubscription(subscription);
    }
}


