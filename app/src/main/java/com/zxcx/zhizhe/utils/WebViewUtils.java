package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.BuildConfig;
import com.zxcx.zhizhe.R;

/**
 * Created by anm on 2017/9/12.
 */

public class WebViewUtils {

	public static WebView getWebView(Context context) {
		WebView webView = new WebView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(params);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);//支持js
		webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webSettings.setSupportZoom(false); //支持缩放
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
		webSettings.setSupportMultipleWindows(false); //多窗口
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(
				WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);//内容加载混合模式，适用于https和http混合时使用
		}
		webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
		webView.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
		webView.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
		if (BuildConfig.DEBUG) {
			WebView.setWebContentsDebuggingEnabled(true);//开启WebView内容调试
		}
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if (request.getUrl().toString().contains("pages")){
					view.loadUrl(request.getUrl().toString());
					return true;
				}
				return true;
			}
		});

		boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
		if (isNight) {
			webView.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
		}

		return webView;
	}

}
