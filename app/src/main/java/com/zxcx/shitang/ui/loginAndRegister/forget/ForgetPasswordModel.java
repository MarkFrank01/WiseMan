package com.zxcx.shitang.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.NullPostSubscriber;

public class ForgetPasswordModel extends BaseModel<ForgetPasswordContract.Presenter> {
    public ForgetPasswordModel(@NonNull ForgetPasswordContract.Presenter present) {
        this.mPresent = present;
    }

    public void changePassword(String phone, String code, String password, int appType){
        mDisposable = AppClient.getAPIService().changePassword(phone,code,password,appType)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


