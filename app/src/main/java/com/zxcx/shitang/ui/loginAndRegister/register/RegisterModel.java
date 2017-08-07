package com.zxcx.shitang.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;


public class RegisterModel extends BaseModel<RegisterContract.Presenter> {

    public RegisterModel(@NonNull RegisterContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        subscription = AppClient.getAPIService().phoneRegistered(phone,code,password,appType,appChannel,appVersion)
                .compose(this.<BaseBean<RegisterBean>>io_main())
                .compose(this.<RegisterBean>handleResult())
                .subscribeWith(new BaseSubscriber<RegisterBean>(mPresent) {
                    @Override
                    public void onNext(RegisterBean registerBean) {
                        mPresent.getDataSuccess(registerBean);
                    }
                });
        addSubscription(subscription);
    }

}


