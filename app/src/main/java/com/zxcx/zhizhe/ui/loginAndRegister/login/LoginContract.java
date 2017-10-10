package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.zxcx.zhizhe.mvpBase.MvpView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

public interface LoginContract {

    interface View extends MvpView<LoginBean> {

    }

    interface Presenter extends IGetPresenter<LoginBean> {

    }
}

