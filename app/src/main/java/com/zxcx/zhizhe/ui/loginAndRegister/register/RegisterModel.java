package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;


public class RegisterModel extends BaseModel<RegisterContract.Presenter> {

    public RegisterModel(@NonNull RegisterContract.Presenter present) {
        this.mPresenter = present;
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneRegistered(phone,code,password,appType,appChannel,appVersion)
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


