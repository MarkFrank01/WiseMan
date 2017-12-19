package com.zxcx.zhizhe.ui.my.aboutUS;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareDialog;
import com.zxcx.zhizhe.ui.welcome.WebViewActivity;
import com.zxcx.zhizhe.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @BindView(R.id.tv_about_us_versions)
    TextView mTvAboutUsVersions;
    @BindView(R.id.tv_about_us_new_versions)
    TextView mTvAboutUsNewVersions;
    @BindView(R.id.tv_about_us_logo)
    TextView mTvAboutUsLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        initToolBar("关于我们");
        mTvAboutUsVersions.setText(Utils.getAppVersionName(this));
        mTvAboutUsLogo.setText(getString(R.string.tv_about_us_logo,Utils.getAppVersionName(this)));

        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            mTvAboutUsNewVersions.setText(upgradeInfo.versionName);
        }
    }

    @OnClick(R.id.ll_about_us_check_update)
    public void onMLlAboutUsCheckUpdateClicked() {
        Beta.checkUpgrade(true, false);
    }

    @OnClick(R.id.ll_about_us_visit)
    public void onMLlAboutUsVisitClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.visit));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.visit_url));
        startActivity(intent);
    }

    @OnClick(R.id.tv_about_us_share)
    public void onMTvAboutUsShareClicked() {
        ShareDialog shareCardDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.app_name));
        bundle.putString("text", "只看实用知识");
        bundle.putString("url", "https://www.pgyer.com/bm8b");
        bundle.putString("imageUrl","http://zhizhe-prod.oss-cn-shenzhen.aliyuncs.com/Icon_1024.png");
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.tv_about_us_agreement)
    public void onMTvAboutUsAgreementClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.agreement));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.agreement_url));
        startActivity(intent);
    }
}
