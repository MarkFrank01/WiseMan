package com.zxcx.zhizhe.ui.card.card.share;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/5/27.
 */

public class ShareWayDialog extends BaseDialog {


    private Unbinder mUnbinder;
    private Context mContext;
    private DefaultShareDialogListener mListener;

    public interface DefaultShareDialogListener {
        void onDefaultShareClick();
        void onDiyShareClick();
    }

    public void setListener(DefaultShareDialogListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share_way, container);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mContext = getActivity();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(12), 0, ScreenUtils.dip2px(12), ScreenUtils.dip2px(10));
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    @OnClick(R.id.tv_dialog_share_default)
    public void onMTvDialogShareDefaultClicked() {
        mListener.onDefaultShareClick();
        dismiss();
    }

    @OnClick(R.id.tv_dialog_share_diy)
    public void onMTvDialogShareDiyClicked() {
        mListener.onDiyShareClick();
        dismiss();
    }

    @OnClick(R.id.tv_dialog_share_cancel)
    public void onMTvDialogShareCancelClicked() {
        dismiss();
    }
}
