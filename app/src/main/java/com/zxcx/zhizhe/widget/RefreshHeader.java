package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class RefreshHeader extends FrameLayout implements PtrUIHandler {

    private TextView mTvRefreshHeader;
    private ImageView mIvRefreshHeader;
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public RefreshHeader(Context context) {
        super(context);
        initViews();
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    protected void initViews() {
        text = getContext().getString(R.string.common_refresh_complete);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_header, this);

        mIvRefreshHeader = (ImageView) header.findViewById(R.id.iv_refresh_header);

        mTvRefreshHeader = (TextView) header.findViewById(R.id.tv_refresh_header);

        resetView();
    }

    private void resetView() {
        mIvRefreshHeader.setVisibility(VISIBLE);
        mTvRefreshHeader.setVisibility(GONE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

        mIvRefreshHeader.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        ((AnimationDrawable) mIvRefreshHeader.getDrawable()).start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        ((AnimationDrawable) mIvRefreshHeader.getDrawable()).stop();
        mIvRefreshHeader.setVisibility(GONE);
        mTvRefreshHeader.setVisibility(VISIBLE);
        mTvRefreshHeader.setText(text);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
    }
}
