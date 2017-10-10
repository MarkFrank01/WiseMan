package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

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


