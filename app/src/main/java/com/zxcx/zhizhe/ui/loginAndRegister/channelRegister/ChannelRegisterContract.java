package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

public interface ChannelRegisterContract {

	interface View extends GetView<LoginBean> {

	}

	interface Presenter extends IGetPresenter<LoginBean> {

	}
}

