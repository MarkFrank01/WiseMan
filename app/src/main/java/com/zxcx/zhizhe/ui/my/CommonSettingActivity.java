package com.zxcx.zhizhe.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeNightModeEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.ui.my.creation.RichTextEditorActivity;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DataCleanManager;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by anm on 2017/6/26.
 */

public class CommonSettingActivity extends BaseActivity {
    @BindView(R.id.cb_common_setting_push)
    CheckBox mScCommonSettingPush;
    @BindView(R.id.cb_common_setting_night_model)
    CheckBox mScCommonSettingNightModel;
    @BindView(R.id.cb_common_setting_only_wifi)
    CheckBox mScCommonSettingOnlyWifi;
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

    @OnClick(R.id.ll_common_setting_re)
    public void onMLlCommonSettingReClicked() {
        Intent intent = new Intent(this, RichTextEditorActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_common_setting_attention)
    public void onMLlCommonSettingAttentionClicked() {
        int userId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
        if (userId == 0){
            toastShow("请先登录");
            startActivity(new Intent(mActivity, LoginActivity.class));
        }else {
            Intent intent = new Intent(this, SelectAttentionActivity.class);
            startActivity(intent);
        }
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
            Constants.IS_NIGHT = isChecked;
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
