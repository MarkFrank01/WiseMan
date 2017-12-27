package com.zxcx.zhizhe.ui.my.aboutUS;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareDialog;
import com.zxcx.zhizhe.ui.welcome.WebViewActivity;
import com.zxcx.zhizhe.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @BindView(R.id.tv_about_us_logo)
    TextView mTvAboutUsLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        mTvAboutUsLogo.setText(getString(R.string.tv_about_us_logo,Utils.getAppVersionName(this)));
    }

    @OnClick(R.id.ll_about_us_share)
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

    @OnClick(R.id.ll_about_us_agreement)
    public void onMTvAboutUsAgreementClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.agreement));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.agreement_url));
        startActivity(intent);
    }
}
