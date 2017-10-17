package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

public class ForgetPasswordModel extends BaseModel<ForgetPasswordContract.Presenter> {
    public ForgetPasswordModel(@NonNull ForgetPasswordContract.Presenter present) {
        this.mPresenter = present;
    }

    public void changePassword(String phone, String code, String password, int appType){
        mDisposable = AppClient.getAPIService().changePassword(phone,code,password,appType)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


