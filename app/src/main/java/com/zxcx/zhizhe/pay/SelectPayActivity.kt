package com.zxcx.zhizhe.pay

import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.pay.zfb.ZFBEntryActivity
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.wxapi.WXEntryActivity
import kotlinx.android.synthetic.main.activity_select_pay.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/1
 * @Description :
 */
class SelectPayActivity : BaseActivity() {

    private var money: String = ""
    private var circleName = ""
    private var circleId = 0

    private var payWX: Boolean = false
    private var payZFB: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pay)
        initToolBar("支付订单")
        initData()

        initView()
    }

    override fun setListener() {

        if (choose_wx.isChecked){
            payWX = true
        }

        choose_wx.setOnClickListener {
            payWX = true
            payZFB = false
            choose_zfb.isChecked = false
        }

        choose_zfb.setOnClickListener {
            payWX = false
            payZFB = true
            choose_wx.isChecked = false
        }

        goto_pay.setOnClickListener {
            if (payWX){
//                toastShow("微信付")
                mActivity.startActivity(WXEntryActivity::class.java){
                    it.putExtra("circleId",circleId)
                }
                finish()
            }

            if (payZFB){
//                toastShow("支付宝付")
                mActivity.startActivity(ZFBEntryActivity::class.java){
                    it.putExtra("circleId",circleId)
                }
                finish()
            }
        }
    }

    private fun initData() {
        money = intent.getStringExtra("circlePrice")
        circleName = intent.getStringExtra("circleName")
        circleId = intent.getIntExtra("circleId",0)
    }

    private fun initView() {
        money_text.text = money
        money_desc.text = "购买圈子-$circleName 会员 - 12个月"
    }
}