package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.PhoneConfirmEvent;
import com.zxcx.zhizhe.mvpBase.CommonDialog;
import com.zxcx.zhizhe.utils.TextViewUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/21.
 */

public class PhoneConfirmDialog extends CommonDialog {

    Unbinder unbinder;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;
    @BindView(R.id.tv_dialog_phone_confirm_title)
    TextView mTvDialogPhoneConfirmTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_phone_confirm, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String phone = getArguments().getString("phone");
        TextViewUtils.setTextViewColorAndBold(mTvDialogPhoneConfirmTitle,phone,getString(R.string.tv_dialog_phone_confirm_title,phone));
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
        EventBus.getDefault().post(new PhoneConfirmEvent());
        this.dismiss();
    }
}
