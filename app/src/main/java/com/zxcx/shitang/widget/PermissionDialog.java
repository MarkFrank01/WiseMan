package com.zxcx.shitang.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.zxcx.shitang.mvpBase.BaseDialog;

/**
 * Created by anm on 2017/7/21.
 */

public class PermissionDialog extends BaseDialog{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("权限提醒");
        builder.setMessage("权限被拒绝，无法继续进行操作，请手动授予权限");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                Intent intent = new
                        Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionDialog.this.dismiss();
            }
        });

        return builder.create();
    }
}
