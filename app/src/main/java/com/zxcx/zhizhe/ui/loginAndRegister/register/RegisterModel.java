package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;


public class RegisterModel extends BaseModel<RegisterContract.Presenter> {

    public RegisterModel(@NonNull RegisterContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneRegistered(phone,code,password,appType,appChannel,appVersion)
                .compose(this.<BaseBean<RegisterBean>>io_main())
                .compose(this.<RegisterBean>handleResult())
                .subscribeWith(new BaseSubscriber<RegisterBean>(mPresent) {
                    @Override
                    public void onNext(RegisterBean registerBean) {
                        mPresent.getDataSuccess(registerBean);
                    }
                });
        addSubscription(mDisposable);
    }

}


