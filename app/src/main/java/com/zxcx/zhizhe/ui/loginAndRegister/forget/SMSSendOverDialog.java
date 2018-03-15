package com.zxcx.zhizhe.ui.loginAndRegister.forget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.CommonDialog;

/**
 * Created by anm on 2017/7/13.
 */

public class SMSSendOverDialog extends CommonDialog {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_sms_send_over, container);
        return view;
    }
}
