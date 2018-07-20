package com.zxcx.zhizhe.mvpBase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kingja.loadsir.core.LoadService;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.LoadingDialog;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by anm on 2017/9/11.
 */

public class BaseDialog extends DialogFragment implements IBasePresenter {
	
	public FragmentActivity mActivity;
	public LoadService loadService;
	protected Disposable mDisposable;
	private LoadingDialog mLoadingDialog;
	private CompositeDisposable mCompositeSubscription;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		return null;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mActivity = getActivity();
		mLoadingDialog = new LoadingDialog();
		setListener();
	}
	
	public void setListener() {
	
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		onUnsubscribe();
		mActivity = null;
		clearLeaks();
		super.onDestroy();
	}
	
	public Toolbar initToolBar(View view, String title) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
		toolbar_title.setText(title);
		return toolbar;
	}
	
	public Toolbar initToolBar(View view, @StringRes int title) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
		toolbar_title.setText(title);
		return toolbar;
	}
	
	public void toastShow(int resId) {
		String text = getString(resId);
		toastShow(text);
	}
	
	public void toastShow(String text) {
		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity)
			.inflate(R.layout.toast, null);
		TextView tvToast = linearLayout.findViewById(R.id.tv_toast);
		Toast toast = new Toast(mActivity);
		toast.setView(linearLayout);
		tvToast.setText(text);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public void toastError(int resId) {
		String text = getString(resId);
		toastError(text);
	}
	
	public void toastError(String text) {
		toastShow(text);
	}
	
	public void clearLeaks() {
	
	}
	
	public void onUnsubscribe() {
		if (mCompositeSubscription != null) {
			mCompositeSubscription.dispose();//取消注册，以避免内存泄露
		}
	}
	
	public void addSubscription(Disposable subscription) {
		if (mCompositeSubscription == null) {
			mCompositeSubscription = new CompositeDisposable();
		}
		mCompositeSubscription.add(subscription);
	}
	
	@Override
	public void showLoading() {
		if (mLoadingDialog != null && !mLoadingDialog.isAdded()) {
			mLoadingDialog.show(mActivity.getSupportFragmentManager(), "");
		}
	}
	
	@Override
	public void hideLoading() {
		if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
			mLoadingDialog.dismiss();
		}
	}
	
	@Override
	public void startLogin() {
		ZhiZheUtils.logout();
		toastShow(R.string.login_timeout);
		startActivity(new Intent(mActivity, LoginActivity.class));
		if (loadService != null) {
			loadService.showCallback(LoginTimeoutCallback.class);
		}
		dismiss();
	}
	
	public boolean checkLogin() {
		if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
			return true;
		} else {
			startActivity(new Intent(mActivity, LoginActivity.class));
			return false;
		}
	}
	
	public void toastFail(String msg) {
		toastShow(msg);
	}
}
