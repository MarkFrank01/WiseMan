package com.zxcx.zhizhe.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.widget.WelcomeSkipView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 启动广告页
 */

public class WelcomeActivity extends BaseActivity implements IGetPresenter<List<ADBean>>,
	WelcomeSkipView.onFinishListener {
	
	@BindView(R.id.iv_welcome_ad)
	ImageView mIvWelcomeAd;
	@BindView(R.id.wsv_welcome_skip)
	WelcomeSkipView mWsvWelcomeSkip;
	private Handler mHandler = new Handler();
	private int mCount = 3;
	
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mCount--;
			if (mCount <= 0) {
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
		
		mWsvWelcomeSkip.setListener(this);
		
		getImage();
		String adImageUrl = SharedPreferencesUtil.getString(SVTSConstants.adImageUrl, "");
		if (!StringUtils.isEmpty(adImageUrl)) {
			ImageLoader.load(this, adImageUrl, R.color.white_final, mIvWelcomeAd);
			mWsvWelcomeSkip.setVisibility(View.VISIBLE);
		} else {
			mHandler.post(mRunnable);
		}
	}
	
	@Override
	public void initStatusBar() {
		//覆盖父类修改状态栏方法
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true);
        mImmersionBar.init();
	}
	
	@Override
	public void onBackPressed() {
		//该页面不允许退出
	}
	
	@Override
	protected void onDestroy() {
		mRunnable = null;
		mHandler = null;
		super.onDestroy();
	}
	
	private void getImage() {
		getAD("100");
	}
	
	/**
	 * 判断是否是第一次启动App
	 *
	 * @return 是-true 否-false
	 */
	private boolean isFirstLaunchApp() {
		return Utils.getIsFirstLaunchApp();
	}
	
	@OnClick(R.id.iv_welcome_ad)
	public void onMIvWelcomeAdClicked() {
		String adImageUrl = SharedPreferencesUtil.getString(SVTSConstants.adImageUrl, "");
		if (!StringUtils.isEmpty(adImageUrl)) {
			String adUrl = SharedPreferencesUtil.getString(SVTSConstants.adUrl, "");
			String adTitle = SharedPreferencesUtil.getString(SVTSConstants.adTitle, "");
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("hasAd", true);
			intent.putExtra("title", adTitle);
			intent.putExtra("url", adUrl);
			startActivity(intent);
			mHandler.removeCallbacks(mRunnable);
			mWsvWelcomeSkip.stop();
			finish();
		}
	}
	
	@OnClick(R.id.wsv_welcome_skip)
	public void onMTvWelcomeSkipClicked() {
		//提前结束动画
		mWsvWelcomeSkip.stop();
		gotoMainActivity();
	}
	
	private void gotoMainActivity() {
		if (isFirstLaunchApp()) {
			Intent intent = new Intent(this, GuidePageActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			Bundle bundle = getIntent().getBundleExtra("push");
			if (bundle != null) {
				intent.putExtra("push", bundle);
			}
			startActivity(intent);
		}
		mHandler.removeCallbacks(mRunnable);
		finish();
	}
	
	public void getAD(String adNum) {
		mDisposable = AppClient.getAPIService().getAD(adNum)
			.compose(BaseRxJava.INSTANCE.io_main())
			.compose(BaseRxJava.INSTANCE.handleArrayResult())
			.subscribeWith(new BaseSubscriber<List<ADBean>>(this) {
				@Override
				public void onNext(List<ADBean> list) {
					getDataSuccess(list);
				}
			});
	}
	
	@Override
	public void getDataSuccess(List<ADBean> list) {
		if (list.size() > 0) {
			ADBean bean = list.get(0);
			if (!this.isDestroyed()) {
				ImageLoader.load(this, bean.getTitleImage(), R.color.white_final, mIvWelcomeAd);
			}
			SharedPreferencesUtil.saveData(SVTSConstants.adImageUrl, bean.getTitleImage());
			SharedPreferencesUtil.saveData(SVTSConstants.adUrl, bean.getBehavior());
			SharedPreferencesUtil.saveData(SVTSConstants.adTitle, bean.getDescription());
		} else {
			SharedPreferencesUtil.saveData(SVTSConstants.adImageUrl, "");
			SharedPreferencesUtil.saveData(SVTSConstants.adUrl, "");
			SharedPreferencesUtil.saveData(SVTSConstants.adTitle, "");
		}
	}
	
	@Override
	public void getDataFail(String msg) {
		toastShow(msg);
	}
	
	@Override
	public void onAnimationFinish() {
		gotoMainActivity();
	}
}
