package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;

public class LoginModel extends BaseModel<LoginContract.Presenter> {

    public LoginModel(@NonNull LoginContract.Presenter present) {
        this.mPresenter = present;
    }

    public void phoneLogin(String phone, String password, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().phoneLogin(phone,password,appType,appChannel,appVersion)
                .compose(BaseRxJava.<BaseBean<LoginBean>>io_main_loading(mPresenter))
                .compose(BaseRxJava.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        mPresenter.getDataSuccess(loginBean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void channelLogin(int channelType, String openId, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().channelLogin(channelType,openId,appType,appChannel,appVersion)
                .compose(BaseRxJava.<BaseBean<LoginBean>>io_main_loading(mPresenter))
                .compose(BaseRxJava.<LoginBean>handleResult())
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        mPresenter.getDataSuccess(loginBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mPresenter.hideLoading();
                        if (t.getMessage() != null) {
                            String code = t.getMessage().substring(0, 3);
                            try {
                                int code1 = Integer.parseInt(code);
                                String message = t.getMessage().substring(3);
                                t.printStackTrace();
                                LogCat.d(t.getMessage());
                                if (Constants.TOKEN_OUTTIME == code1) {
                                    mPresenter.channelLoginNeedRegister();
                                } else {
                                    mPresenter.getDataFail(message);
                                }
                            } catch (NumberFormatException e) {
                                mPresenter.getDataFail(App.getContext().getString(R.string.network_error));
                            }
                        }else {
                            mPresenter.getDataFail(App.getContext().getString(R.string.network_error));
                        }
                    }
                });
        addSubscription(mDisposable);
    }
}


