package com.zxcx.zhizhe.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CanNotSaveDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NeedSaveDialog
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.UploadingDialog
import kotlinx.android.synthetic.main.activity_creation_editor.*
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

/**
 * 通用网页加载页面
 */

class WebViewActivity : BaseActivity(), ADMoreWindow.ADMoreListener {

	internal var mWebView: WebView? = null
	private var title = ""
	private var url = ""
	private var loadUrl = ""
	private var imageUrl: String? = null
	private lateinit var adMoreWindow: ADMoreWindow

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_web_view)

		adMoreWindow = ADMoreWindow(mActivity)
		adMoreWindow.mListener = this
		initWebView()
		getIntentData()
	}

	private fun initWebView() {
		mWebView = WebViewUtils.getWebView(this)

		ll_web_view.addView(mWebView)
		//添加方法给js调用
		mWebView?.addJavascriptInterface(this, "native")
	}

	@JavascriptInterface
	fun enterCreate() {
		runOnUiThread {
			if (checkLogin()){
				when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
					writer_status_writer -> {
						//创作界面
						mActivity.startActivity(CreationEditorActivity::class.java) {}
						finish()
					}
					else -> {
						val dialog = CreationAgreementDialog()
						dialog.mListener = {
							mActivity.startActivity(CreationEditorActivity::class.java) {}
							finish()
						}
						dialog.show(mActivity.supportFragmentManager, "")
					}
				}
			}
		}
	}


	@JavascriptInterface
	fun writeMessage() {
		runOnUiThread {
			initToolBar("奖励兑换页")
			iv_toolbar_right.visibility = View.GONE
		}
	}

	@JavascriptInterface
	fun openIntelligence() {
		runOnUiThread {
			initToolBar("智力值说明")
			iv_toolbar_right.visibility = View.GONE
		}
	}

	@JavascriptInterface
	fun setToolBarTitle(meg:String="智者") {
		runOnUiThread {
			initToolBar(meg)
			iv_toolbar_right.visibility = View.GONE
		}
	}

	@JavascriptInterface
	fun submitSuccess() {
		runOnUiThread {
			toastShow("提交成功")
			iv_toolbar_right.visibility = View.VISIBLE
			initToolBar(title)
			mWebView?.goBack()

		}
	}

	@JavascriptInterface
	fun saveFail() {
		runOnUiThread {
			toastError("提交失败")
		}
	}



	private fun getIntentData() {
		title = intent.getStringExtra("title")
		url = intent.getStringExtra("url")
		imageUrl = intent.getStringExtra("imageUrl")
		val isAD = intent.getBooleanExtra("isAD", false)
		loadUrl = if (isAD && SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
			url + "?token=" + SharedPreferencesUtil.getString(SVTSConstants.token, "")
		} else {
			url +"?token=0"
		}
		initToolBar(title)
		iv_toolbar_right.visibility = if (isAD) View.VISIBLE else View.GONE
		iv_toolbar_right.setImageResource(R.drawable.iv_toolbar_more)
		iv_toolbar_right.setOnClickListener {
			adMoreWindow.showAsDropDown(iv_toolbar_right, 0, -ScreenUtils.dip2px(20f))
		}

		mWebView?.loadUrl(loadUrl)
	}

	override fun onBackPressed() {
		if (adMoreWindow.isShowing) {
			adMoreWindow.dismiss()
		}
		if (mWebView?.canGoBack() == true) {
			mWebView?.goBack()
			iv_toolbar_right.visibility = View.VISIBLE
			initToolBar(title)
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
		bundle.putString("url", url +"?token=share")
		bundle.putString("imageUrl", imageUrl)
		shareDialog.arguments = bundle
		shareDialog.show(supportFragmentManager, "")
	}
}
