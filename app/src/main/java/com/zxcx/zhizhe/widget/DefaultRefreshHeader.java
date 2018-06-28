package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.zxcx.zhizhe.R;

public class DefaultRefreshHeader extends FrameLayout implements
	com.scwang.smartrefresh.layout.api.RefreshHeader {
	
	private TextView mTvRefreshHeader;
	private ImageView mIvRefreshHeader;
	private String text;
	
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
		
		View header = LayoutInflater.from(getContext())
			.inflate(R.layout.layout_refresh_header, this);
		
		mIvRefreshHeader = (ImageView) header.findViewById(R.id.iv_refresh_header);
		
		mTvRefreshHeader = (TextView) header.findViewById(R.id.tv_refresh_header);
		
		resetView();
	}
	
	private void resetView() {
		mIvRefreshHeader.setVisibility(VISIBLE);
		mTvRefreshHeader.setVisibility(GONE);
	}
	
	@NonNull
	@Override
	public View getView() {
		return this;
	}
	
	@NonNull
	@Override
	public SpinnerStyle getSpinnerStyle() {
		return SpinnerStyle.Translate;
	}
	
	@Override
	public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height,
		int extendHeight) {
		mIvRefreshHeader.setVisibility(VISIBLE);
		((AnimationDrawable) mIvRefreshHeader.getDrawable()).start();
	}
	
	@Override
	public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
		((AnimationDrawable) mIvRefreshHeader.getDrawable()).stop();
		mIvRefreshHeader.setVisibility(GONE);
		mTvRefreshHeader.setVisibility(VISIBLE);
		mTvRefreshHeader.setText(text);
		return 200;
	}
	
	@Override
	public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState,
		RefreshState newState) {
		switch (newState) {
			case None:
			case PullDownToRefresh:
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
	
	}
	
	@Override
	public void onReleasing(float percent, int offset, int height, int extendHeight) {
	
	}
	
	@Override
	public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
	
	}
	
	@Override
	public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
	
	}
	
	@Override
	public boolean isSupportHorizontalDrag() {
		return false;
	}
}
