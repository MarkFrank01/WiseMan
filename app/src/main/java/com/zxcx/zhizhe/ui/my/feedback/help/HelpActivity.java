package com.zxcx.zhizhe.ui.my.feedback.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.my.feedback.feedback.FeedbackActivity;
import com.zxcx.zhizhe.ui.welcome.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initToolBar("帮助");
        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("反馈");
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onMTvToolbarRightClicked() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_1)
    public void onMLlHelp1Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_1));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_1_url));
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_2)
    public void onMLlHelp2Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_2));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_2_url));
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_3)
    public void onMLlHelp3Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_3));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_3_url));
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_4)
    public void onMLlHelp4Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_4));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_4_url));
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_5)
    public void onMLlHelp5Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_5));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_5_url));
        startActivity(intent);
    }

    @OnClick(R.id.ll_help_6)
    public void onMLlHelp6Clicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title",getString(R.string.help_6));
        intent.putExtra("url",getString(R.string.base_url)+getString(R.string.help_6_url));
        startActivity(intent);
    }
}
