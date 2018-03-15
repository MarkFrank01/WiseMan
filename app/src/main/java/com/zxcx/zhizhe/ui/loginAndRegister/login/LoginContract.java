package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;

public interface LoginContract {

    interface View extends GetView<LoginBean> {
        void channelLoginSuccess(LoginBean bean);
        void channelLoginNeedRegister();
        void needRegister();
        void smsCodeVerificationSuccess(SMSCodeVerificationBean bean);
    }

    interface Presenter extends IGetPresenter<LoginBean> {
        void channelLoginSuccess(LoginBean bean);
        void channelLoginNeedRegister();
        void needRegister();
        void smsCodeVerificationSuccess(SMSCodeVerificationBean bean);
    }
}

