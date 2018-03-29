package com.zxcx.zhizhe.ui.my.creation.creationDetails

import android.content.Intent
import android.os.Bundle
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
import com.zxcx.zhizhe.event.SaveFreedomNoteSuccessEvent
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.retrofit.APIService
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_reject_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RejectDetailsActivity : MvpActivity<RejectDetailsPresenter>(), RejectDetailsContract.View {

    private var mWebView: WebView? = null
    private var cardId: Int = 0
    private var cardBagId: Int = 0
    private var name: String? = null
    private var author: String? = null
    private var imageUrl: String? = null
    private var date: String? = null
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

        mPresenter.getRejectDetails(cardId)
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

    override fun onReload(v: View) {
        mPresenter.getRejectDetails(cardId)
        mWebView?.reload()
    }

    override fun getDataSuccess(bean: RejectDetailsBean) {
        //进入时只有id的时候，在这里初始化界面
        name = bean.name
        imageUrl = bean.imageUrl
        date = DateTimeUtils.getDateString(bean.date)
        author = bean.authorName
        cardBagId = bean.cardBagId
        val rejectReason = bean.rejectReason
        tv_reject_reason?.text = rejectReason
        tv_reject_details_title?.text = name
        tv_reject_details_info?.text = getString(R.string.tv_card_info, date, author)
        tv_reject_details_card_bag?.text = bean.cardBagName
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_reject_details)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SaveFreedomNoteSuccessEvent) {
        finish()
    }

    private fun initData() {
        cardId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
        imageUrl = intent.getStringExtra("imageUrl")
        date = intent.getStringExtra("date")
        author = intent.getStringExtra("author")
    }

    private fun initView() {
        if (!StringUtils.isEmpty(name))
            tv_reject_details_title?.text = name
        if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(date))
            tv_reject_details_info?.text = getString(R.string.tv_card_info, date, author)
        if (!StringUtils.isEmpty(imageUrl))
            ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_reject_details)

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
                tv_reject_reedit?.visibility = View.VISIBLE
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
        if (isNight) {
            mUrl = APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + cardId + "?fontSize=" + fontSize
        } else {
            mUrl = APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + cardId + "?fontSize=" + fontSize

        }
        mWebView?.loadUrl(mUrl)

        initLoadSir()
    }

    override fun setListener() {
        iv_reject_details_back.setOnClickListener {
            onBackPressed()
        }

        tv_reject_reedit.setOnClickListener {
            val intent = Intent(mActivity, NewCreationTitleActivity::class.java)
            intent.putExtra("cardId", cardId)
            intent.putExtra("cardBagId", cardBagId)
            intent.putExtra("title", name)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
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
