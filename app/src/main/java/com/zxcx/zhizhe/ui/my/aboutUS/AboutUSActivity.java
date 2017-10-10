package com.zxcx.zhizhe.ui.my.aboutUS;

import android.os.Bundle;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @BindView(R.id.tv_about_us_versions)
    TextView mTvAboutUsVersions;
    @BindView(R.id.tv_about_us_new_versions)
    TextView mTvAboutUsNewVersions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        initToolBar("关于我们");
        mTvAboutUsVersions.setText(Utils.getAppVersionName(this));

        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            mTvAboutUsNewVersions.setText(upgradeInfo.versionName);
        }
    }

    @OnClick(R.id.ll_about_us_check_update)
    public void onMLlAboutUsCheckUpdateClicked() {
        Beta.checkUpgrade(true,false);
    }

    @OnClick(R.id.ll_about_us_visit)
    public void onMLlAboutUsVisitClicked() {
    }

    @OnClick(R.id.tv_about_us_share)
    public void onMTvAboutUsShareClicked() {
    }
}
