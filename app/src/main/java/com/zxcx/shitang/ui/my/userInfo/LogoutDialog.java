package com.zxcx.shitang.ui.my.userInfo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zxcx.shitang.event.LogoutEvent;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by anm on 2017/7/6.
 */

public class LogoutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("登出后就没办法收藏了！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.saveData(SVTSConstants.userId,0);
                SharedPreferencesUtil.saveData(SVTSConstants.nickName,"");
                SharedPreferencesUtil.saveData(SVTSConstants.birthday,"");
                EventBus.getDefault().post(new LogoutEvent());
                dialog.dismiss();
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
