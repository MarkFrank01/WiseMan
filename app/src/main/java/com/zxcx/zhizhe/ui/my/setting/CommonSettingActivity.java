package com.zxcx.zhizhe.ui.my.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeNightModeEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.my.aboutUS.AboutUSActivity;
import com.zxcx.zhizhe.ui.my.feedback.feedback.FeedbackActivity;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DataCleanManager;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/6/26.
 */

public class CommonSettingActivity extends BaseActivity {
    @BindView(R.id.cb_common_setting_night_model)
    CheckBox mScCommonSettingNightModel;
    @BindView(R.id.tv_common_setting_clean_cache)
    TextView mTvCommonSettingCleanCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        ButterKnife.bind(this);

        initToolBar("设置");
        updateCacheSize();
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        mScCommonSettingNightModel.setChecked(isNight);
        mScCommonSettingNightModel.setOnCheckedChangeListener(new OnNightModeCheckChange());
    }

    private void updateCacheSize() {
        try {
            mTvCommonSettingCleanCache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_common_setting_night_model)
    public void onMLlCommonSettingNightModelClicked() {
        mScCommonSettingNightModel.setChecked(!mScCommonSettingNightModel.isChecked());
    }

    @OnClick(R.id.ll_common_setting_clean_cache)
    public void onMLlCommonSettingCleanCacheClicked() {
        DataCleanManager.cleanApplicationData(this);
        updateCacheSize();
    }

    @OnClick(R.id.ll_common_setting_user_info)
    public void onMLlCommonSettingUserInfoClicked() {
        if (checkLogin()) {
            startActivity(new Intent(mActivity, UserInfoActivity.class));
        }
    }

    @OnClick(R.id.ll_common_setting_message)
    public void onMLlCommonSettingMessageClicked() {
        if (checkLogin()) {
            startActivity(new Intent(mActivity, MessageModeActivity.class));
        }
    }

    @OnClick(R.id.ll_common_setting_font)
    public void onMLlCommonSettingFontClicked() {
        startActivity(new Intent(mActivity, TextSizeChangeActivity.class));
    }

    @OnClick(R.id.ll_common_setting_image)
    public void onMLlCommonSettingImageClicked() {
        startActivity(new Intent(mActivity, ImageLoadModeActivity.class));
    }

    @OnClick(R.id.ll_common_setting_versions)
    public void onMLlCommonSettingVersionsClicked() {
        startActivity(new Intent(mActivity, AboutUSActivity.class));
    }

    @OnClick(R.id.ll_common_setting_feedback)
    public void onMLlCommonSettingFeedbackClicked() {
        startActivity(new Intent(mActivity, FeedbackActivity.class));
    }

    @OnClick(R.id.ll_common_setting_evaluate)
    public void onMLlCommonSettingEvaluateClicked() {
        try {
            Uri uri = Uri.parse("market://details?id="+ getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            toastShow("您没有安装应用市场");
        }
    }

    private class OnNightModeCheckChange implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferencesUtil.saveData(SVTSConstants.isNight, isChecked);
            Constants.IS_NIGHT = isChecked;
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
            EventBus.getDefault().post(new ChangeNightModeEvent());
        }
    }
}
