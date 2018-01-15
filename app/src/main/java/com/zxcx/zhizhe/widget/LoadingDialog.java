package com.zxcx.zhizhe.widget;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/5/27.
 */

public class LoadingDialog extends BaseDialog {

    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.layout_empty_loading, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AnimationDrawable) mIvLoading.getDrawable()).start();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.color.translate);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.0f;
            lp.width = ScreenUtils.dip2px(80);
            lp.height = ScreenUtils.dip2px(80);
            window.setAttributes(lp);
        }
    }

    @Override
    public void onDestroyView() {
        ((AnimationDrawable) mIvLoading.getDrawable()).stop();
        super.onDestroyView();
        unbinder.unbind();
    }
}
