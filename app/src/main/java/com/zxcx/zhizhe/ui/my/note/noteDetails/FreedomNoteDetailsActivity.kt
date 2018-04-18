package com.zxcx.zhizhe.ui.my.note.noteDetails

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
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.retrofit.APIService
import com.zxcx.zhizhe.ui.my.note.newNote.NoteEditorActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.WebViewUtils
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_freedom_note_details.*

class FreedomNoteDetailsActivity : BaseActivity() {

    private var mWebView: WebView? = null
    private var noteId: Int = 0
    private var name: String? = null
    private var date: String? = null
    private var mUrl: String? = null
    private var loadService2: LoadService<*>? = null
    private var loadSir2: LoadSir? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freedom_note_details)
        ButterKnife.bind(this)

        initData()
        initView()
    }

    override fun initStatusBar() {

    }

    override fun onDestroy() {
        mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        mWebView?.clearHistory()

        (mWebView?.parent as ViewGroup).removeView(mWebView)
        mWebView?.destroy()
        mWebView = null
        super.onDestroy()
    }

    override fun onReload(v: View) {
        mWebView?.reload()
    }

    private fun initData() {
        noteId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
    }

    private fun initView() {
        tv_note_details_title.text = name
        tv_note_details_info.text = getString(R.string.tv_item_card_note_info,
                intent.getStringExtra("date"),"记录")

        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(this)

        mWebView?.isFocusable = false
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
                ll_note_details_bottom.visibility = View.VISIBLE
                view_line.visibility = View.VISIBLE
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                isError = true
                loadService.showSuccess()
                if (loadService2 == null) {
                    loadService2 = loadSir2?.register(rl_note_details, this@FreedomNoteDetailsActivity)
                }
                loadService2?.showCallback(CardDetailsNetworkErrorCallback::class.java)
            }
        }
        fl_note_details.addView(mWebView)
        val isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false)
        val fontSize = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue, 1)
        mUrl = if (isNight) {
            APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + noteId + "?fontSize=" + fontSize
        } else {
            APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + noteId + "?fontSize=" + fontSize
        }
        mWebView?.loadUrl(mUrl)

        initLoadSir()
    }

    override fun setListener() {
        iv_note_details_back.setOnClickListener {
            onBackPressed()
        }

        iv_note_details_share.setOnClickListener {
            gotoShare(mUrl)
        }

        iv_note_details_edit.setOnClickListener {
            startActivity(NoteEditorActivity::class.java,{
                it.putExtra("noteId",noteId)
                it.putExtra("title",name)
            })
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

    private fun gotoShare(url: String?) {
        val shareDialog = ShareFreedomNoteDialog()
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("url", url)
        bundle.putString("date", date)
        shareDialog.arguments = bundle
        shareDialog.show(fragmentManager, "")
    }
}
