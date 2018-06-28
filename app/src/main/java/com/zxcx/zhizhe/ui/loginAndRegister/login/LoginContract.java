package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

public interface LoginContract {

	interface View extends GetView<LoginBean> {

		void channelLoginSuccess(LoginBean bean);

		void channelLoginNeedRegister();
	}

	interface Presenter extends IGetPresenter<LoginBean> {

		void channelLoginSuccess(LoginBean bean);

		void channelLoginNeedRegister();
	}
}

