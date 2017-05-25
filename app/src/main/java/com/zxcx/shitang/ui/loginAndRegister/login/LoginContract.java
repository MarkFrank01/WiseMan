package com.zxcx.shitang.ui.loginAndRegister.login;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginBean;

public interface LoginContract {

    interface View extends MvpView<LoginBean> {

    }

    interface Presenter extends IBasePresenter<LoginBean> {

    }
}

