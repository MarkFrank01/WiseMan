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

    public void phoneRegister(String phone, String verifyKey, String jpushRID, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneRegistered(phone,verifyKey,jpushRID,password,appType,appChannel,appVersion)
                .compose(BaseRxJava.INSTANCE.<LoginBean>handleResult())
                .compose(BaseRxJava.INSTANCE.<LoginBean>io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

}


