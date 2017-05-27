package com.zxcx.shitang.ui.welcome;

import android.os.Bundle;
import android.os.Handler;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.retrofit.APIService;
import com.zxcx.shitang.retrofit.AppClient;

public class WelcomeActivity extends BaseActivity {

    private Handler mHandler = new Handler();
    private int mCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getImage();
    }

    private void getImage() {
        AppClient.retrofit().create(APIService.class);
        mHandler.post(mRunnable);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mCount--;
            if (mCount == 0){

            }else {
                mHandler.postDelayed(mRunnable,1000);
            }
        }
    };

}
