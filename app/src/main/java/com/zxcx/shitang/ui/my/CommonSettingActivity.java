package com.zxcx.shitang.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.shitang.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by anm on 2017/6/26.
 */

public class CommonSettingActivity extends BaseActivity {
    @BindView(R.id.sc_common_setting_push)
    SwitchCompat mScCommonSettingPush;
    @BindView(R.id.sc_common_setting_night_model)
    SwitchCompat mScCommonSettingNightModel;
    @BindView(R.id.sc_common_setting_only_wifi)
    SwitchCompat mScCommonSettingOnlyWifi;
    @BindView(R.id.tv_common_setting_clean_cache)
    TextView mTvCommonSettingCleanCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        ButterKnife.bind(this);

        initToolBar("通用设置");
        updateCacheSize();
        mScCommonSettingPush.setChecked(!JPushInterface.isPushStopped(this));
    }

    @OnClick(R.id.ll_common_setting_attention)
    public void onMLlCommonSettingAttentionClicked() {
        Intent intent = new Intent(this, SelectAttentionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_common_setting_push)
    public void onMLlCommonSettingPushClicked() {
        mScCommonSettingPush.setChecked(!mScCommonSettingPush.isChecked());
        if (JPushInterface.isPushStopped(this)){
            JPushInterface.resumePush(this);
        }else {
            JPushInterface.stopPush(this);
        }
    }

    @OnClick(R.id.ll_common_setting_night_model)
    public void onMLlCommonSettingNightModelClicked() {
        mScCommonSettingNightModel.setChecked(!mScCommonSettingNightModel.isChecked());
    }

    @OnClick(R.id.ll_common_setting_only_wifi)
    public void onMLlCommonSettingOnlyWifiClicked() {
        mScCommonSettingOnlyWifi.setChecked(!mScCommonSettingOnlyWifi.isChecked());
    }

    @OnClick(R.id.ll_common_setting_clean_cache)
    public void onMLlCommonSettingCleanCacheClicked() {
        DataCleanManager.cleanApplicationData(this);
        updateCacheSize();
        toastShow("清理成功");
    }

    private void updateCacheSize() {
        try {
            mTvCommonSettingCleanCache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
