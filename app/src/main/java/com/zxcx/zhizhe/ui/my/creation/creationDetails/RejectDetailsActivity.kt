package com.zxcx.zhizhe.ui.my.creation.creationDetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.DeleteCreationEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.retrofit.APIService
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_reject_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 未通过长文详情
 */

class RejectDetailsActivity : MvpActivity<RejectDetailsPresenter>(), RejectDetailsContract.View {

	private var mWebView: WebView? = null
	private lateinit var cardBean: CardBean
	private var mUrl: String? = null
	private var loadService2: LoadService<*>? = null
	private var loadSir2: LoadSir? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		setContentView(R.layout.activity_reject_details)
		super.onCreate(savedInstanceState)
		ButterKnife.bind(this)
		EventBus.getDefault().register(this)

		initData()
		initView()

		mPresenter.getRejectDetails(cardBean.id)
	}

	override fun initStatusBar() {

	}

	override fun onDestroy() {
		if (mWebView != null) {
			mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
			mWebView?.clearHistory()

			(mWebView?.parent as ViewGroup).removeView(mWebView)
			mWebView?.destroy()
			mWebView = null
		}
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun createPresenter(): RejectDetailsPresenter {
		return RejectDetailsPresenter(this)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: CommitCardReviewEvent) {
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: DeleteCreationEvent) {
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: SaveDraftSuccessEvent) {
		finish()
	}

	override fun onReload(v: View?) {
		mPresenter.getRejectDetails(cardBean.id)
		mWebView?.reload()
	}

	override fun getDataSuccess(bean: CardBean) {
		//进入时只有id的时候，在这里初始化界面
		cardBean = bean

		tv_reject_details_title.text = bean.name
		tv_reject_details_date.text = DateTimeUtils.getDateString(cardBean.date)
		tv_reject_details_category.text = bean.categoryName
		tv_reject_details_label.text = bean.getLabelName()
		val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_reject_details)

		val rejectReason = bean.rejectReason
		tv_reject_reason?.text = rejectReason
	}

	override fun postSuccess() {
		toastShow("删除成功")
		onBackPressed()
	}

	override fun postFail(msg: String?) {
		toastError(msg)
	}

	private fun initData() {
		cardBean = intent.getParcelableExtra("cardBean")
	}

	private fun initView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			tv_reject_agreement?.text = Html.fromHtml("详情请阅读<font color='#0088AA'>智者创作协议</font>", Html.FROM_HTML_MODE_LEGACY)
		} else {
			tv_reject_agreement?.text = Html.fromHtml("详情请阅读<font color='#0088AA'>智者创作协议</font>")
		}
		if (!StringUtils.isEmpty(cardBean.name))
			tv_reject_details_title.text = cardBean.name
		if (!StringUtils.isEmpty(DateTimeUtils.getDateString(cardBean.date)))
			tv_reject_details_date.text = DateTimeUtils.getDateString(cardBean.date)
		if (!StringUtils.isEmpty(cardBean.categoryName))
			tv_reject_details_category.text = cardBean.categoryName
		if (!StringUtils.isEmpty(cardBean.getLabelName()))
			tv_reject_details_label.text = cardBean.getLabelName()
		if (!StringUtils.isEmpty(cardBean.imageUrl)) {
			val imageUrl = ZhiZheUtils.getHDImageUrl(cardBean.imageUrl)
			ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_reject_details)
		}

		initWebView()

		initLoadSir()
	}

	private fun initWebView() {
		//获取WebView，并将WebView高度设为WRAP_CONTENT
		mWebView = WebViewUtils.getWebView(this)
		val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		mWebView?.layoutParams = params

		mWebView?.webViewClient = object : WebViewClient() {

			internal var isError = false

			override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
				return true
			}

			override fun onPageFinished(view: WebView, url: String) {
				if (isError) {
					isError = false
					return
				}
				if (loadService != null) {
					loadService.showSuccess()
					loadService = null
				}
				if (loadService2 != null) {
					loadService2?.showSuccess()
				}
			}

			override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
				super.onReceivedError(view, request, error)
				isError = true
				loadService.showSuccess()
				if (loadService2 == null) {
					loadService2 = loadSir2?.register(sv_reject_details, this@RejectDetailsActivity)
				}
				loadService2?.showCallback(CardDetailsNetworkErrorCallback::class.java)
			}
		}
		fl_reject_details?.addView(mWebView)
		val isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false)
		val fontSize = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue, 1)
		mUrl = if (isNight) {
			APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + cardBean.id + "?fontSize=" + fontSize
		} else {
			APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + cardBean.id + "?fontSize=" + fontSize

		}
		mWebView?.loadUrl(mUrl)
	}

	override fun setListener() {
		iv_common_close.setOnClickListener {
			onBackPressed()
		}

		tv_reject_agreement.setOnClickListener {

			startActivity(WebViewActivity::class.java) {
				it.putExtra("title", "智者创作协议")
				if (Constants.IS_NIGHT) {
					it.putExtra("url", getString(R.string.base_url) + getString(R.string.creation_agreement_dark_url))
				} else {
					it.putExtra("url", getString(R.string.base_url) + getString(R.string.creation_agreement_url))
				}
			}
		}

		iv_reject_details_edit.setOnClickListener {
			startActivity(CreationEditorActivity::class.java) {
				it.putExtra("cardId", cardBean.id)
				it.putExtra("type", 2)
			}
		}

		iv_reject_details_delete.setOnClickListener {
			mPresenter.deleteCard(cardBean.id)
		}
	}

	private fun initLoadSir() {
		val loadSir = LoadSir.Builder()
				.addCallback(CardDetailsLoadingCallback())
				.setDefaultCallback(CardDetailsLoadingCallback::class.java)
				.build()
		loadService = loadSir.register(mWebView, this)
		loadSir2 = LoadSir.Builder()
				.addCallback(CardDetailsNetworkErrorCallback())
				.build()
	}
}
