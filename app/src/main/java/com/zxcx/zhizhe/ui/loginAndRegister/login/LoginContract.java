package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.zxcx.zhizhe.mvpBase.MvpView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

public interface LoginContract {

    interface View extends MvpView<LoginBean> {
        void channelLoginSuccess(LoginBean bean);
        void channelLoginNeedRegister();
    }

    interface Presenter extends IGetPresenter<LoginBean> {
        void channelLoginSuccess(LoginBean bean);
        void channelLoginNeedRegister();
    }
}

