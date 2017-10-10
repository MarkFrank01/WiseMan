package com.zxcx.zhizhe.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity implements IGetPresenter<List<WelcomeBean>> {

    @BindView(R.id.iv_welcome_ad)
    ImageView mIvWelcomeAd;
    @BindView(R.id.tv_welcome_skip)
    TextView mTvWelcomeSkip;
    private Handler mHandler = new Handler();
    private int mCount = 4;

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
        String adImageUrl = SharedPreferencesUtil.getString(SVTSConstants.adImageUrl,"");
        if (!StringUtils.isEmpty(adImageUrl)){
            ImageLoader.load(this, adImageUrl,mIvWelcomeAd);
            mTvWelcomeSkip.setVisibility(View.VISIBLE);
        }else {
            mCount = 2;
        }
        mHandler.post(mRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getImage() {
        getAD("101");
    }

    @OnClick(R.id.iv_welcome_ad)
    public void onMIvWelcomeAdClicked() {
        String adImageUrl = SharedPreferencesUtil.getString(SVTSConstants.adImageUrl,"");
        if (!StringUtils.isEmpty(adImageUrl)){
            String adUrl = SharedPreferencesUtil.getString(SVTSConstants.adUrl,"");
            String adTitle = SharedPreferencesUtil.getString(SVTSConstants.adTitle,"");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("hasAd",true);
            intent.putExtra("title",adTitle);
            intent.putExtra("url",adUrl);
            startActivity(intent);
            mHandler.removeCallbacks(mRunnable);
            finish();
        }
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

    public void getAD(String adNum) {
        mDisposable = AppClient.getAPIService().getAD(adNum)
                .compose(BaseRxJava.<BaseArrayBean<WelcomeBean>>io_main())
                .compose(BaseRxJava.<WelcomeBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<WelcomeBean>>(this) {
                    @Override
                    public void onNext(List<WelcomeBean> list) {
                        getDataSuccess(list);
                    }
                });
    }

    @Override
    public void getDataSuccess(List<WelcomeBean> list) {
        if (list.size()>0) {
            WelcomeBean bean = list.get(0);
            if (!this.isDestroyed()) {
                ImageLoader.load(this, bean.getContent(), mIvWelcomeAd);
                mTvWelcomeSkip.setVisibility(View.VISIBLE);
            }
            SharedPreferencesUtil.saveData(SVTSConstants.adImageUrl,bean.getContent());
            SharedPreferencesUtil.saveData(SVTSConstants.adUrl,bean.getBehavior());
            SharedPreferencesUtil.saveData(SVTSConstants.adTitle,bean.getDescription());
        }else {
            SharedPreferencesUtil.saveData(SVTSConstants.adImageUrl,"");
            SharedPreferencesUtil.saveData(SVTSConstants.adUrl,"");
            SharedPreferencesUtil.saveData(SVTSConstants.adTitle,"");
        }
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }
}
