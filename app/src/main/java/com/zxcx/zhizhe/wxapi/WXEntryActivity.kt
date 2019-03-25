package com.zxcx.zhizhe.wxapi

import android.os.Bundle
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.pay.wx.WXBean
import com.zxcx.zhizhe.pay.wx.WXEntryContract
import com.zxcx.zhizhe.pay.wx.WXPresenter
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_entry_wxpay.*

/**
 * @author : MarkFrank01
 * @Created on 2019/2/23
 * @Description : 待处理问题解决
 */
class WXEntryActivity : MvpActivity<WXPresenter>(), WXEntryContract.View {

    private var circleId: Int = 0


    // IWXAPI 是第三方app和微信通信的openapi接口
    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_wxpay)

        initData()

//        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
//
//        api.registerApp(Constants.WX_APP_ID)

//        goto_send_btn.setOnClickListener {
//            goto_send_btn.isEnabled = false
//
//            //待服务器那边处理
//            //此均为测试写法
//            var req = PayReq()
//            req.appId = Constants.WX_APP_ID
//            req.partnerId = "1900006771"
//            req.prepayId = "Sign=WXPay"
//            req.nonceStr = "18844964480834c91cef8b15e56b0a4d"
//            req.timeStamp = "1550898756"
//            req.packageValue = "Sign=WXPay"
//            req.sign = "96C707EFD84B2EBE72889EBA07009EAD"
//            req.extData = "app data" // optional
//            api.sendReq(req)
//
//
//            goto_send_btn.isEnabled = true
//        }

        goto_send_btn.setOnClickListener {
            //            mPresenter.getWxOrderPay(circleId)

            Thread {
                //                //使用正式的
//                api = WXAPIFactory.createWXAPI(this, "wxac8cb0c3f9b06b05", false)
//                api.registerApp("wxac8cb0c3f9b06b05")
//
//                var req = PayReq()
//                req.appId = "wxac8cb0c3f9b06b05"
//                req.partnerId = "1526397881"
//                req.prepayId = "wx2514133485969684201fd5623868406246"
//                req.nonceStr = "544728c51385f9f0e01907a0798ef0ec"
//                req.timeStamp = "1550898756"
//                req.packageValue = "Sign=WXPay"
//                req.sign = "B3D15F5C0C10A6B9DF7CC32068057048"
//                req.extData = "app data"
////        api.sendReq(req)
//
//                LogCat.e("CNM"+req.checkArgs())
//                LogCat.e("send return "+api.sendReq(req))

                api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1", false)
                api.registerApp("wxb4ba3c02aa476ea1")

                var req = PayReq()
                req.appId = "wxb4ba3c02aa476ea1"
                req.partnerId = "1900006771"
                req.prepayId = "wx25113721872209321da28db90063508880"
                req.nonceStr = "544728c51385f9f0e01907a0798ef0ec"
                req.timeStamp = "1553485041"
                req.packageValue = "Sign=WXPay"
                req.sign = "3E3F5CDD3158E8083680D52DBD623916"
                req.extData = "app data"
                api.sendReq(req)


            }.start()


        }

    }

    override fun createPresenter(): WXPresenter {
        return WXPresenter(this)
    }

    override fun getWxOrderPaySuccess(bean: WXBean) {
        toastShow("SUCCESS")
        LogCat.e("SIGN is " + bean.sign)
        LogCat.e("ID is " + bean.prepayId)
        holdOnWXPay(bean.sign, bean.prepayId)
    }

    override fun getDataSuccess(bean: WXBean?) {
    }

    fun initData() {
        circleId = intent.getIntExtra("circleId", 0)
    }

    fun holdOnWXPay(sign: String, prepayId: String) {

//        //使用正式的
//        api = WXAPIFactory.createWXAPI(this, "wxac8cb0c3f9b06b05", false)
//        api.registerApp("wxac8cb0c3f9b06b05")
//
//        var req = PayReq()
//        req.appId = "wxac8cb0c3f9b06b05"
//        req.partnerId = "1526397881"
//        req.prepayId = prepayId
//        req.nonceStr = "544728c51385f9f0e01907a0798ef0ec"
//        req.timeStamp = "1550898756"
//        req.packageValue = "Sign=WXPay"
//        req.sign = sign
//        req.extData = "app data"
////        api.sendReq(req)
//
//        LogCat.e("CNM"+req.checkArgs())
//        LogCat.e("send return "+api.sendReq(req))

////使用测试的成功
//        toastShow("准备起来")
//        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1",false)
//        api.registerApp( "wxb4ba3c02aa476ea1")
//
//        var req = PayReq()
//        req.appId = "wxb4ba3c02aa476ea1"
//        req.partnerId = "1900006771"
//        req.prepayId = "wx25113721872209321da28db90063508880"
//        req.nonceStr = "544728c51385f9f0e01907a0798ef0ec"
//        req.timeStamp = "1553485041"
//        req.packageValue = "Sign=WXPay"
//        req.sign = "3E3F5CDD3158E8083680D52DBD623916"
//        req.extData = "app data"
//        api.sendReq(req)

//        var req = PayReq()
//        req.appId = Constants.WX_APP_ID
//        req.partnerId = "1526397881"
//        req.prepayId = prepayId
//        req.nonceStr = "18844964480834c91cef8b15e56b0a4d"
//        req.sign = sign
//        req.timeStamp = "1550898756"
//        req.packageValue = "Sign=WXPay"
//        req.extData = "app data" // optional
//        api.sendReq(req)
    }
}