package com.zxcx.zhizhe.ui.my.setting;

import android.os.Bundle;
import android.widget.CheckBox;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class MessageModeActivity extends BaseActivity {

    @BindView(R.id.cb_message_mode_system)
    CheckBox mCbMessageModeSystem;
    @BindView(R.id.cb_message_mode_dynamic)
    CheckBox mCbMessageModeDynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mode);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.ll_message_mode_system)
    public void onMLlImageLoadModeWifiClicked() {
        mCbMessageModeSystem.setChecked(!mCbMessageModeSystem.isChecked());
    }

    @OnClick(R.id.ll_message_mode_dynamic)
    public void onMLlImageLoadModeAllClicked() {
        mCbMessageModeDynamic.setChecked(!mCbMessageModeDynamic.isChecked());
    }

    @OnCheckedChanged(R.id.cb_message_mode_system)
    public void onCbSystemChanged() {

    }

    @OnCheckedChanged(R.id.cb_message_mode_dynamic)
    public void onCbDynamicChanged() {

    }


}
