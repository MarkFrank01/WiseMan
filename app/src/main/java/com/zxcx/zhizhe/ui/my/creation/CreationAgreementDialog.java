package com.zxcx.zhizhe.ui.my.creation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreationAgreementDialog extends BaseDialog {


    @BindView(R.id.fl_dialog_creation_agreement)
    FrameLayout mFlDialogCreationAgreement;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.ll_creation_agreement)
    LinearLayout mLlCreationAgreement;
    private WebView mWebView;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_creation_agreement, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.color.translate);
        /*window.getDecorView().setPadding(ScreenUtils.dip2px(10), ScreenUtils.dip2px(84),
                ScreenUtils.dip2px(10), ScreenUtils.dip2px(84));*/
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(20);
            lp.height = ScreenUtils.getScreenHeight() - ScreenUtils.dip2px(168);
            window.setAttributes(lp);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        unbinder.unbind();
        super.onDestroy();
    }

    private void initView() {

        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mLlCreationAgreement.setVisibility(View.VISIBLE);
                mViewLine.setVisibility(View.VISIBLE);
            }
        });
        mFlDialogCreationAgreement.addView(mWebView);
        if (Constants.IS_NIGHT){
            mWebView.loadUrl(getString(R.string.base_url) + getString(R.string.creation_agreement_dark_url));
        }else {
            mWebView.loadUrl(getString(R.string.base_url) + getString(R.string.creation_agreement_url));
        }
    }

    @OnClick(R.id.tv_dialog_cancel)
    public void onMTvDialogCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.tv_dialog_confirm)
    public void onMTvDialogConfirmClicked() {
        startActivity(new Intent(getActivity(), ApplyForCreation1Activity.class));
        dismiss();
    }
}
