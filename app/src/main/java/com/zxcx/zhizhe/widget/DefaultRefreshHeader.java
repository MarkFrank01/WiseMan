package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

public class DefaultRefreshHeader extends FrameLayout implements
	com.scwang.smartrefresh.layout.api.RefreshHeader {
	
	private TextView mTvRefreshHeader;
	private LottieAnimationView mIvRefreshHeader;
	private String text;
	private View header;
	private boolean mFinished;
	
	public DefaultRefreshHeader(Context context) {
		super(context);
		initViews();
	}
	
	public DefaultRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}
	
	public DefaultRefreshHeader(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	protected void initViews() {
		text = getContext().getString(R.string.common_refresh_complete);
		
		header = LayoutInflater.from(getContext())
			.inflate(R.layout.layout_refresh_header, this);
		
		mIvRefreshHeader = header.findViewById(R.id.iv_refresh_header);
		
		mTvRefreshHeader = header.findViewById(R.id.tv_refresh_header);
		
		resetView();
	}
	
	private void resetView() {
		mIvRefreshHeader.setAnimation("loading.json");
		mIvRefreshHeader.setRepeatCount(LottieDrawable.INFINITE);
		mTvRefreshHeader.setVisibility(GONE);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		header.offsetTopAndBottom(-ScreenUtils.dip2px(80));
	}
	
	@NonNull
	@Override
	public View getView() {
		return this;
	}
	
	@NonNull
	@Override
	public SpinnerStyle getSpinnerStyle() {
		return SpinnerStyle.FixedFront;
	}
	
	@Override
	public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height,
		int extendHeight) {
//		mIvRefreshHeader.setVisibility(VISIBLE);
		mTvRefreshHeader.setVisibility(GONE);
		mIvRefreshHeader.setAnimation("loading.json");
		mIvRefreshHeader.setRepeatCount(LottieDrawable.INFINITE);
		mIvRefreshHeader.playAnimation();
//		((AnimationDrawable) mIvRefreshHeader.getDrawable()).start();
	}
	
	@Override
	public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
//		((AnimationDrawable) mIvRefreshHeader.getDrawable()).stop();
		mIvRefreshHeader.setRepeatCount(0);
		mIvRefreshHeader.setAnimation("load_complete.json");
		mIvRefreshHeader.playAnimation();
		mFinished = true;
		postDelayed(() -> {
			mTvRefreshHeader.setVisibility(VISIBLE);
			mTvRefreshHeader.setText(text);
		}, 500);
		postDelayed(() -> {
			header.animate().translationY(0);
		}, 1500);
		return 1500;
	}
	
	@Override
	public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState,
		RefreshState newState) {
		switch (newState) {
			case None:
			case PullDownToRefresh:
				mFinished = false;
				resetView();
				break;
		}
	}
	
	@Override
	public void setPrimaryColors(int... colors) {
	
	}
	
	@Override
	public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
	
	}
	
	@Override
	public void onPulling(float percent, int offset, int height, int extendHeight) {
		header.setTranslationY(offset);
	}
	
	@Override
	public void onReleasing(float percent, int offset, int height, int extendHeight) {
		if (!mFinished) {
			onPulling(percent, offset, height, extendHeight);
		}
	}
	
	@Override
	public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
		header.animate().translationY(ScreenUtils.dip2px(80));
	}
	
	@Override
	public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
	
	}
	
	@Override
	public boolean isSupportHorizontalDrag() {
		return false;
	}
}
