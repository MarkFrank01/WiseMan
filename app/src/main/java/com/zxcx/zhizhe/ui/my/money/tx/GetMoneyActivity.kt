package com.zxcx.zhizhe.ui.my.money.tx

import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class GetMoneyActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_money)

        initView()
    }

    private fun initView(){
        initToolBar("体现")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.isEnabled = false
    }
}