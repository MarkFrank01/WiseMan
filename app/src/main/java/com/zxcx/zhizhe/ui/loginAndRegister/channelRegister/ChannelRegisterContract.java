package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.MvpView;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

public interface ChannelRegisterContract {

    interface View extends MvpView<LoginBean> {

    }

    interface Presenter extends IGetPresenter<LoginBean> {

    }
}

