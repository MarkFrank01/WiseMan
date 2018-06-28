package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

class ChannelRegisterModel extends BaseModel<ChannelRegisterContract.Presenter> {

	ChannelRegisterModel(@NonNull ChannelRegisterContract.Presenter present) {
		this.mPresenter = present;
	}

	void channelRegister(int channelType, String openId, String userIcon, String name, Integer sex,
		String birthday, String phone, String smsCode, String jpushRID, int appType,
		String appChannel, String appVersion) {
		mDisposable = AppClient.getAPIService().channelRegister(channelType, openId, userIcon, name,
			sex, birthday, phone, smsCode, jpushRID, appType, appChannel, appVersion)
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

	void changePhone(String phone, String verifyKey) {
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
}


