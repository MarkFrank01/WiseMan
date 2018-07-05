package com.zxcx.zhizhe.ui.my.followUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.UnFollowConfirmEvent;
import com.zxcx.zhizhe.mvpBase.CommonDialog;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by anm on 2017/7/21.
 */

public class UnFollowConfirmDialog extends CommonDialog {
	
	Unbinder unbinder;
	@BindView(R.id.tv_dialog_logout_title)
	TextView tvDialogLogoutTitle;
	@BindView(R.id.tv_dialog_cancel)
	TextView tvDialogCancel;
	@BindView(R.id.tv_dialog_confirm)
	TextView tvDialogConfirm;
	
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
		tvDialogLogoutTitle.setText("是否取消对该作者的关注");
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
		EventBus.getDefault().post(new UnFollowConfirmEvent(getArguments().getInt("userId")));
		this.dismiss();
	}
}
