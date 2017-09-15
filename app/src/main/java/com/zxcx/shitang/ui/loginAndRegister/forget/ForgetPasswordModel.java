package com.zxcx.shitang.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.PostSubscriber;

public class ForgetPasswordModel extends BaseModel<ForgetPasswordContract.Presenter> {
    public ForgetPasswordModel(@NonNull ForgetPasswordContract.Presenter present) {
        this.mPresent = present;
    }

    public void phoneRegister(String phone, String code, String password, int appType, String appChannel, String appVersion){
        subscription = AppClient.getAPIService().phoneRegistered(phone,code,password,appType,appChannel,appVersion)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }
}


