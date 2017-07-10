package com.zxcx.shitang.ui.my.aboutUS;

import android.os.Bundle;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @BindView(R.id.tv_about_us_versions)
    TextView mTvAboutUsVersions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        initToolBar("关于我们");
        mTvAboutUsVersions.setText(Utils.getAppVersionName(this));
    }

    @OnClick(R.id.ll_about_us_check_update)
    public void onMLlAboutUsCheckUpdateClicked() {
    }

    @OnClick(R.id.ll_about_us_visit)
    public void onMLlAboutUsVisitClicked() {
    }

    @OnClick(R.id.tv_about_us_share)
    public void onMTvAboutUsShareClicked() {
    }
}
