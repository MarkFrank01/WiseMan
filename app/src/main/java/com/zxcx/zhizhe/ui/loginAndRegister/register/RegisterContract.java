package com.zxcx.zhizhe.ui.loginAndRegister.register;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

public interface RegisterContract {

    interface View extends GetView<LoginBean> {
        void getPhoneStatusSuccess(boolean isRegistered);
        void smsCodeVerificationSuccess(SMSCodeVerificationBean bean);
    }

    interface Presenter extends IGetPresenter<LoginBean> {
        void getPhoneStatusSuccess(boolean isRegistered);
        void smsCodeVerificationSuccess(SMSCodeVerificationBean bean);
    }
}

