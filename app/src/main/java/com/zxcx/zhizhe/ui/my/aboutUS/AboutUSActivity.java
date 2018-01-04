package com.zxcx.zhizhe.ui.my.aboutUS;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareDialog;
import com.zxcx.zhizhe.ui.welcome.WebViewActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @BindView(R.id.tv_about_us_logo)
    TextView mTvAboutUsLogo;
    @BindView(R.id.fl_about_us)
    FrameLayout mFlAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_us);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mTvAboutUsLogo.setText(getString(R.string.tv_about_us_logo, Utils.getAppVersionName(this)));
    }

    @Override
    public void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        if (!Constants.IS_NIGHT) {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .titleBarMarginTop(R.id.cl_about_us);
        } else {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .fitsSystemWindows(true)
                    .titleBarMarginTop(R.id.cl_about_us);
            Drawable drawable = new ColorDrawable(ContextCompat.getColor(mActivity,R.color.opacity_30));
            mFlAboutUs.setForeground(drawable);
        }
        mImmersionBar.init();
    }

    @OnClick(R.id.ll_about_us_share)
    public void onMTvAboutUsShareClicked() {
        ShareDialog shareCardDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.app_name));
        bundle.putString("text", "只看实用知识");
        bundle.putString("url", "https://www.pgyer.com/bm8b");
        bundle.putString("imageUrl", "http://zhizhe-prod.oss-cn-shenzhen.aliyuncs.com/Icon_1024.png");
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.ll_about_us_agreement)
    public void onMTvAboutUsAgreementClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title", getString(R.string.agreement));
        if (Constants.IS_NIGHT){
            intent.putExtra("url", getString(R.string.base_url) + getString(R.string.agreement_dark_url));
        }else {
            intent.putExtra("url", getString(R.string.base_url) + getString(R.string.agreement_url));
        }
        startActivity(intent);
    }

    @OnClick(R.id.iv_toolbar_back)
    public void onMIvToolbarBackClicked() {
        onBackPressed();
    }
}
