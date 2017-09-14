package com.zxcx.shitang.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.iv_welcome_ad)
    ImageView mIvWelcomeAd;
    @BindView(R.id.tv_welcome_skip)
    TextView mTvWelcomeSkip;
    private Handler mHandler = new Handler();
    private int mCount = 1;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mCount--;
            if (mCount == 0) {
                gotoMainActivity();
            } else {
                mHandler.postDelayed(mRunnable, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        getImage();


    }

    private void getImage() {
//        AppClient.retrofit().create(APIService.class);
        mHandler.post(mRunnable);
    }

    @OnClick(R.id.iv_welcome_ad)
    public void onMIvWelcomeAdClicked() {



        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("hasAd",true);
        intent.putExtra("title","百度");
        intent.putExtra("url","https://www.baidu.com");
        startActivity(intent);
        mHandler.removeCallbacks(mRunnable);
        finish();
    }

    @OnClick(R.id.tv_welcome_skip)
    public void onMTvWelcomeSkipClicked() {
        gotoMainActivity();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = getIntent().getBundleExtra("push");
        if (bundle != null){
            intent.putExtra("push",bundle);
        }
        startActivity(intent);
        mHandler.removeCallbacks(mRunnable);
        finish();
    }
}
