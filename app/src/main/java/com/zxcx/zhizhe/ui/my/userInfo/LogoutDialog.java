package com.zxcx.zhizhe.ui.my.userInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.CommonDialog;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/6.
 */

public class LogoutDialog extends CommonDialog {

	@BindView(R.id.tv_dialog_logout)
	TextView mTvDialogLogout;
	@BindView(R.id.tv_dialog_cancel)
	TextView mTvDialogCancel;
	@BindView(R.id.tv_dialog_confirm)
	TextView mTvDialogConfirm;
	@BindView(R.id.tv_dialog_logout_title)
	TextView mTvDialogLogoutTitle;
	private Unbinder unbinder;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_logout, container);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R.id.tv_dialog_cancel)
	public void onMTvDialogCancelClicked() {
		this.dismiss();
	}

	@OnClick(R.id.tv_dialog_confirm)
	public void onMTvDialogConfirmClicked() {
		ZhiZheUtils.logout();
		this.dismiss();
		getActivity().finish();
	}
}
