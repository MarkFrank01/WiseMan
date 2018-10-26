package com.zxcx.zhizhe.ui.my.creation.creationDetails

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.retrofit.APIService
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_review_details.*

/**
 * 审核中长文详情
 */

class ReviewDetailsActivity : MvpActivity<RejectDetailsPresenter>(), RejectDetailsContract.View {

    private var mWebView: WebView? = null
    private var loadService2: LoadService<*>? = null
    private var loadSir2: LoadSir? = null
    private lateinit var cardBean: CardBean

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_review_details)
        super.onCreate(savedInstanceState)

        initData()
        initView()

        mPresenter.getReviewDetails(cardBean.id)
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
        super.onDestroy()
    }

    override fun createPresenter(): RejectDetailsPresenter {
        return RejectDetailsPresenter(this)
    }

    override fun onReload(v: View) {
        mPresenter.getReviewDetails(cardBean.id)
        mWebView?.reload()
    }

    override fun getDataSuccess(bean: CardBean) {
        //进入时只有id的时候，在这里初始化界面
        cardBean = bean

        tv_review_details_title.text = bean.name
        tv_review_details_date.text = DateTimeUtils.getDateString(cardBean.date)
        tv_review_details_category.text = bean.categoryName
        tv_review_details_label.text = bean.getLabelName()
        if (bean.secondCollectionTitle != "" && bean.secondCollectionTitle.isNotEmpty()) {
            tv_review_details_label2.visibility = View.VISIBLE
            tv_review_details_label2.text = bean.getSecondLabelName()
        }
        val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_review_details)
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initData() {
        cardBean = intent.getParcelableExtra("cardBean")
    }

    private fun initView() {
        if (!StringUtils.isEmpty(cardBean.name))
            tv_review_details_title.text = cardBean.name
        if (!StringUtils.isEmpty(DateTimeUtils.getDateString(cardBean.date)))
            tv_review_details_date.text = DateTimeUtils.getDateString(cardBean.date)
        if (!StringUtils.isEmpty(cardBean.categoryName))
            tv_review_details_category.text = cardBean.categoryName
        if (!StringUtils.isEmpty(cardBean.getLabelName()))
            tv_review_details_label.text = cardBean.getLabelName()
        if (!StringUtils.isEmpty(cardBean.imageUrl)) {
            val imageUrl = ZhiZheUtils.getHDImageUrl(cardBean.imageUrl)
            ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_review_details)
        }
        if (cardBean.secondCollectionTitle != "" && cardBean.secondCollectionTitle.isNotEmpty()) {
            tv_review_details_label2.visibility = View.VISIBLE
            tv_review_details_label2.text = cardBean.getSecondLabelName()
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
                    loadService2 = loadSir2?.register(sv_review_details, this@ReviewDetailsActivity)
                }
                loadService2?.showCallback(CardDetailsNetworkErrorCallback::class.java)
            }
        }
        mWebView?.isFocusable = false
        fl_review_details.addView(mWebView)
        val url: String
        val isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false)
        val fontSize = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue, 1)
        url = if (isNight) {
            APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + cardBean.id + "?fontSize=" + fontSize
        } else {
            APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + cardBean.id + "?fontSize=" + fontSize

        }
        mWebView?.loadUrl(url)
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
