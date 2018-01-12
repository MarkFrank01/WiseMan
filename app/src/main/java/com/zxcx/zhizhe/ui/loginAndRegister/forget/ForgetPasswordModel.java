package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;

public class ForgetPasswordModel extends BaseModel<ForgetPasswordContract.Presenter> {
    public ForgetPasswordModel(@NonNull ForgetPasswordContract.Presenter present) {
        this.mPresenter = present;
    }

    public void forgetPassword(String phone, String verifyKey, String jpushRID, String password, int appType){
        mDisposable = AppClient.getAPIService().forgetPassword(phone,verifyKey,jpushRID,password,appType)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void channelRegister(int channelType, String openId, String password, String userIcon, String name, Integer sex,
                                String birthday, String phone, String verifyKey, String jpushRID, int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().channelRegister(channelType,openId,password,userIcon,name,
                sex,birthday,phone,verifyKey,jpushRID,appType,appChannel,appVersion)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<LoginBean>(mPresenter) {
                    @Override
                    public void onNext(LoginBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void changePhone(String phone, String verifyKey) {
        mDisposable = AppClient.getAPIService().changePhone(phone, verifyKey)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.getDataSuccess(new LoginBean());
                    }
                });
        addSubscription(mDisposable);
    }

    public void checkPhoneRegistered(String phone){
        mDisposable = AppClient.getAPIService().checkPhoneRegistered(phone)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean loginBean) {
                        mPresenter.getPhoneStatusSuccess(false);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t.getMessage() != null) {
                            String code = t.getMessage().substring(0, 3);
                            try {
                                int code1 = Integer.parseInt(code);
                                String message = t.getMessage().substring(3);
                                t.printStackTrace();
                                LogCat.d(t.getMessage());
                                if (Constants.NEED_LOGIN == code1) {
                                    mPresenter.getPhoneStatusSuccess(true);
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

    public void smsCodeVerification(String phone, String code){
        mDisposable = AppClient.getAPIService().smsCodeVerification(phone,code)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new BaseSubscriber<SMSCodeVerificationBean>(mPresenter) {
                    @Override
                    public void onNext(SMSCodeVerificationBean bean) {
                        mPresenter.smsCodeVerificationSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }
}


