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

class IntelligenceValueActivity : BaseActivity(){

    private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intelligence_value)
        initWebView()
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initWebView() {
        mWebView = WebViewUtils.getWebView(this)
        fl_intelligence_value.addView(mWebView,0)
        mWebView?.loadUrl("http://192.168.1.153:8043/view/intelligence")
        mWebView?.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
                mWebView?.evaluateJavascript("setTimeStampAndToken('$token');",null)
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
