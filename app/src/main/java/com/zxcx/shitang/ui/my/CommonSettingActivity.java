package com.zxcx.shitang.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeNightModeEvent;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.shitang.utils.DataCleanManager;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

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
        mScCommonSettingPush.setOnCheckedChangeListener(new OnPushCheckChange());
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        mScCommonSettingNightModel.setChecked(isNight);
        mScCommonSettingNightModel.setOnCheckedChangeListener(new OnNightModeCheckChange());
        boolean isOnlyWifi = SharedPreferencesUtil.getBoolean(SVTSConstants.isOnlyWifi, false);
        mScCommonSettingOnlyWifi.setChecked(isOnlyWifi);
        mScCommonSettingOnlyWifi.setOnCheckedChangeListener(new OnOnlyWifiCheckChange());
    }

    @OnClick(R.id.ll_common_setting_attention)
    public void onMLlCommonSettingAttentionClicked() {
        Intent intent = new Intent(this, SelectAttentionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_common_setting_push)
    public void onMLlCommonSettingPushClicked() {
        mScCommonSettingPush.setChecked(!mScCommonSettingPush.isChecked());
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

    private class OnPushCheckChange implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (JPushInterface.isPushStopped(mActivity)){
                JPushInterface.resumePush(mActivity);
            }else {
                JPushInterface.stopPush(mActivity);
            }
        }
    }

    private class OnNightModeCheckChange implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferencesUtil.saveData(SVTSConstants.isNight,isChecked);
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            EventBus.getDefault().post(new ChangeNightModeEvent());
            recreate();
        }
    }

    private class OnOnlyWifiCheckChange implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferencesUtil.saveData(SVTSConstants.isOnlyWifi,isChecked);
        }
    }
}
