package com.zxcx.zhizhe.ui.my.intelligenceValue

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.WebViewUtils
import kotlinx.android.synthetic.main.activity_intelligence_value.*

/**
 * 我的-智力值页面
 */

class IntelligenceValueActivity : BaseActivity() {

	private var mWebView: WebView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_intelligence_value)
		initToolBar("智力值")
		initWebView()
	}

	override fun setListener() {
	}

	private fun initWebView() {
		mWebView = WebViewUtils.getWebView(this)
		fl_intelligence_value.addView(mWebView, 0)
		val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.intelligence_url)
		mWebView?.loadUrl(url)
		mWebView?.webViewClient = object : WebViewClient() {

			override fun onPageFinished(view: WebView, url: String) {
				val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
				mWebView?.evaluateJavascript("setTimeStampAndToken('$token');", null)
			}
		}
	}

	override fun onDestroy() {
		mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
		mWebView?.clearHistory()

		(mWebView?.parent as ViewGroup).removeView(mWebView)
		mWebView?.destroy()
		mWebView = null
		super.onDestroy()
	}
}
