package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;

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

    public void channelLogin(int channelType, String openId, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().channelLogin(channelType,openId,appType,appChannel,appVersion)
                .compose(this.<BaseBean<LoginBean>>io_main())
                .compose(this.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresent) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        mPresent.getDataSuccess(loginBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t.getMessage() != null) {
                            String code = t.getMessage().substring(0, 3);
                            String message = t.getMessage().substring(3);
                            t.printStackTrace();
                            LogCat.d(t.getMessage());
                            if (String.valueOf(Constants.NEED_LOGIN).equals(code)) {
                                mPresent.channelLoginNeedRegister();
                            } else {
                                mPresent.getDataFail(message);
                            }
                        }else {
                            mPresent.getDataFail(App.getContext().getString(R.string.network_error));
                        }
                    }
                });
        addSubscription(mDisposable);
    }
}


