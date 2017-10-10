package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

public class LoginModel extends BaseModel<LoginContract.Presenter> {

    public LoginModel(@NonNull LoginContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneLogin(String phone, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneLogin(phone,password,appType,appChannel,appVersion)
                .compose(this.<BaseBean<LoginBean>>io_main())
                .compose(this.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresent) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        mPresent.getDataSuccess(loginBean);
                    }
                });
        addSubscription(mDisposable);
    }
}


