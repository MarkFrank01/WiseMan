package com.zxcx.zhizhe.ui.my.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.my.aboutUS.AboutUSActivity;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoActivity;
import com.zxcx.zhizhe.utils.DataCleanManager;

/**
 * Created by anm on 2017/6/26.
 */

public class CommonSettingActivity extends BaseActivity {

	@BindView(R.id.tv_common_setting_clean_cache)
	TextView mTvCommonSettingCleanCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_setting);
		ButterKnife.bind(this);
		initToolBar("设置");
		updateCacheSize();
	}

	private void updateCacheSize() {
		try {
			mTvCommonSettingCleanCache.setText(DataCleanManager.getTotalCacheSize(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
