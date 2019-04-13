package com.zxcx.zhizhe.ui.circle.circlemore

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.activity_introduction.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/9
 * @Description :
 */
class CircleIntroductionActivity :BaseActivity() {
    private var mIntroduction:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        initToolBar("圈子介绍")
        initData()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            create_xieyi?.text = Html.fromHtml("如需了解更多规则，请点击查阅<font color='#0088AA'>圈子协议</font>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            create_xieyi?.text = Html.fromHtml("如需了解更多规则，请点击查阅<font color='#0088AA'>圈子协议</font>")
        }

        tv_show.text = mIntroduction
    }

    private fun initData(){
        mIntroduction = intent.getStringExtra("info")
    }
}