package com.zxcx.zhizhe.ui.welcome

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.utils.WebViewUtils
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar.*

class WebViewActivity : BaseActivity(), ADMoreWindow.ADMoreListener {

	internal var mWebView: WebView? = null
	private var title = ""
	private var url = ""
	private var imageUrl = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_web_view)
		ButterKnife.bind(this)

		initWebView()
		getIntentData()
	}

	private fun initWebView() {
		mWebView = WebViewUtils.getWebView(this)

		ll_web_view.addView(mWebView)
	}

	private fun getIntentData() {
		title = intent.getStringExtra("title")
		url = intent.getStringExtra("url")
		imageUrl = intent.getStringExtra("imageUrl")
		val isAD = intent.getBooleanExtra("isAD", false)
		initToolBar(title)
		iv_toolbar_right.visibility = if (isAD) View.VISIBLE else View.GONE
		iv_toolbar_right.setImageResource(R.drawable.tv_home_rank)
		iv_toolbar_right.setOnClickListener {
			val adMoreWindow = ADMoreWindow(mActivity)
			adMoreWindow.mListener = this
		}

		mWebView?.loadUrl(url)
	}

	override fun onBackPressed() {
		if (mWebView?.canGoBack() == true) {
			mWebView?.goBack()
		} else {
			finish()
		}
	}

	override fun onDestroy() {
		if (mWebView != null) {
			mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
			mWebView?.clearHistory()

			(mWebView?.parent as ViewGroup).removeView(mWebView)
			mWebView?.destroy()
			mWebView = null
		}
		super.onDestroy()
	}

	override fun refresh() {
		mWebView?.reload()
	}

	override fun share() {
		val shareDialog = ShareDialog()
		val bundle = Bundle()
		bundle.putString("title", title)
		bundle.putString("url", url)
		bundle.putString("imageUrl", imageUrl)
		shareDialog.arguments = bundle
		shareDialog.show(fragmentManager, "")
	}
}
