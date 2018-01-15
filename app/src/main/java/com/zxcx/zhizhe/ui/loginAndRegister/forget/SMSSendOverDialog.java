package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/7/13.
 */

public class SMSSendOverDialog extends BaseDialog {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_sms_send_over, container);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawableResource(R.color.translate);
            window.getDecorView().setPadding(ScreenUtils.dip2px(53), 0, ScreenUtils.dip2px(53), 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }
}
