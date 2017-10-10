package com.zxcx.zhizhe.ui.my.feedback.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.my.feedback.feedback.FeedbackActivity;

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
}
