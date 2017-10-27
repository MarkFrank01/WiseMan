package com.zxcx.zhizhe.ui.my.creation;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseFragment;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PreviewFragment extends BaseFragment {

    WebView mWebView;
    @BindView(R.id.ll_web_view)
    LinearLayout mLlWebView;

    private static final String SETUP_HTML = "file:///android_asset/preview.html";
    Unbinder unbinder;
    private boolean isReady = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preview, container,false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
    }

    private void initWebView() {
        mWebView = WebViewUtils.getWebView(mActivity);
        mWebView.setWebViewClient(new PreviewWebViewClient());
        mLlWebView.addView(mWebView);
        mWebView.loadUrl(SETUP_HTML);
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
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setTitle(String title) {
        if (!StringUtils.isEmpty(title)) {
            try {
                exec("javascript:RE.setTitle('" + URLEncoder.encode(title, "UTF-8") + "');");
            } catch (UnsupportedEncodingException e) {
                // No handling
            }
        }
    }

    public void setContent(String content) {
        if (!StringUtils.isEmpty(content)) {
            try {
                exec("javascript:RE.setBody('" + URLEncoder.encode(content, "UTF-8") + "');");
            } catch (UnsupportedEncodingException e) {
                // No handling
            }
        }
    }

    protected void exec(final String trigger) {
        if (isReady) {
            mWebView.evaluateJavascript(trigger, null);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    protected class PreviewWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            isReady = url.equalsIgnoreCase(SETUP_HTML);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }
    }
}
