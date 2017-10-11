package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;


public class RegisterModel extends BaseModel<RegisterContract.Presenter> {

    public RegisterModel(@NonNull RegisterContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneRegistered(phone,code,password,appType,appChannel,appVersion)
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


