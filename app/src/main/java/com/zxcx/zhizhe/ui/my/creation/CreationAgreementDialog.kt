package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseDialog
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.dialog_creation_agreement.*

class CreationAgreementDialog : BaseDialog(), INullPostPresenter {

	private var mWebView: WebView? = null

	lateinit var mListener: () -> Unit


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		dialog.setCanceledOnTouchOutside(true)
		val view = inflater.inflate(R.layout.dialog_creation_agreement, container)
		return view
	}

	override fun onStart() {
		super.onStart()

		val dialog = dialog
		if (dialog != null) {
			val window = dialog.window
			window!!.setBackgroundDrawableResource(R.color.translate)
			/*window.getDecorView().setPadding(ScreenUtils.dip2px(10), ScreenUtils.dip2px(84),
                ScreenUtils.dip2px(10), ScreenUtils.dip2px(84));*/
			val lp = window.attributes
			lp.gravity = Gravity.CENTER
			lp.width = ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f)
			lp.height = ScreenUtils.getDisplayHeight() - ScreenUtils.dip2px(168f)
			window.attributes = lp
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initView()
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

	private fun initView() {

		//获取WebView，并将WebView高度设为WRAP_CONTENT
		mWebView = WebViewUtils.getWebView(activity)
		val params = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		mWebView?.layoutParams = params
		mWebView?.isVerticalScrollBarEnabled = false
		mWebView?.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
				return true
			}

			override fun onPageFinished(view: WebView, url: String) {
				ll_creation_agreement.visibility = View.VISIBLE
			}
		}
		fl_dialog_creation_agreement.addView(mWebView)
		if (Constants.IS_NIGHT) {
			mWebView?.loadUrl(
					getString(R.string.base_url) + getString(R.string.creation_agreement_dark_url))
		} else {
			mWebView?.loadUrl(getString(R.string.base_url) + getString(R.string.creation_agreement_url))
		}
	}

	override fun setListener() {
		super.setListener()
		tv_dialog_cancel.setOnClickListener {
			dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			applyCreation()
		}
	}

	override fun postSuccess() {
		SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, writer_status_writer)
		mListener.invoke()
		dismiss()
	}

	override fun postFail(msg: String?) {
		toastFail(msg)
	}

	private fun applyCreation() {
		mDisposable = AppClient.getAPIService().applyCreation()
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(this) {

					override fun onNext(baseBean: BaseBean<*>) {
						postSuccess()
					}
				})
		addSubscription(mDisposable)
	}
}
