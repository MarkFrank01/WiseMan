package com.zxcx.shitang.ui.my.userInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.LogoutEvent;
import com.zxcx.shitang.mvpBase.BaseDialog;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/6.
 */

public class LogoutDialog extends BaseDialog {

    @BindView(R.id.tv_dialog_logout)
    TextView mTvDialogLogout;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_logout, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextPaint tp = mTvDialogCancel.getPaint();
        tp.setFakeBoldText(true);
        tp = mTvDialogConfirm.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(27.5f), 0, ScreenUtils.dip2px(27.5f), 0);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
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

        SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, (long) 0);
        SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, (long) 0);
        SharedPreferencesUtil.saveData(SVTSConstants.token, "");
        SharedPreferencesUtil.saveData(SVTSConstants.userId, 0);
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, "");
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, "");
        EventBus.getDefault().post(new LogoutEvent());
        this.dismiss();
        getActivity().finish();
    }
}
