package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
	LoginContract.Presenter {

	private final LoginModel mModel;

	public LoginPresenter(@NonNull LoginContract.View view) {
		attachView(view);
		mModel = new LoginModel(this);
	}

	public void smsCodeLogin(String phone, String smsCode, String jpushRID, int appType,
		String appChannel, String appVersion) {
		mModel.smsCodeLogin(phone, smsCode, jpushRID, appType, appChannel, appVersion);
	}

	public void channelLogin(int channelType, String uid, String jpushRID, int appType,
		String appChannel, String appVersion,String openId) {
		mModel.channelLogin(channelType, uid, jpushRID, appType, appChannel, appVersion,openId);
	}

	@Override
	public void getDataSuccess(LoginBean bean) {
		mView.getDataSuccess(bean);
	}

	@Override
	public void getDataFail(String msg) {
		mView.toastFail(msg);
	}

	@Override
	public void channelLoginSuccess(LoginBean bean) {
		mView.channelLoginSuccess(bean);
	}

	@Override
	public void channelLoginNeedRegister() {
		mView.channelLoginNeedRegister();
	}

	@Override
	public void showLoading() {
		mView.showLoading();
	}

	@Override
	public void hideLoading() {
		mView.hideLoading();
	}

	@Override
	public void startLogin() {
		mView.startLogin();
	}

	public void detachView() {
		super.detachView();
		mModel.onDestroy();
	}
}

