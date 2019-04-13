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

        mPresenter.getWxOrderPay(circleId)
//
//        goto_send_btn.setOnClickListener {
//            mPresenter.getWxOrderPay(circleId)
//        }

    }

    override fun createPresenter(): WXPresenter {
        return WXPresenter(this)
    }

    override fun getWxOrderPaySuccess(bean: WXBean) {
//        toastShow("SUCCESS")
//        LogCat.e("SIGN is " + bean.sign)
//        LogCat.e("ID is " + bean.prepayId)
//        LogCat.e("noc is " + bean.nonceStr)
//        LogCat.e("time is " + bean.timestamp)
        holdOnWXPay(bean.appid,bean.partnerid,bean.packageType,bean.nonceStr,bean.timestamp,bean.prepayId,bean.sign)
    }

    override fun getDataSuccess(bean: WXBean?) {
    }

    fun initData() {
        circleId = intent.getIntExtra("circleId", 0)
    }

    fun holdOnWXPay(appId:String,partnerId:String,packageType:String,nonceStr:String,timestamp:String,prepayId:String,sign:String) {

        //使用正式的
        api = WXAPIFactory.createWXAPI(this, appId)
        api.registerApp(appId)

        var req = PayReq()
        req.appId = appId
        req.partnerId = partnerId
        req.prepayId = prepayId
        req.nonceStr = nonceStr
        req.timeStamp = timestamp
        req.packageValue = packageType
        req.sign =sign
        req.extData = "app data"

        api.sendReq(req)

        finish()

//        val request = PayReq()
//        request.appId = "wxac8cb0c3f9b06b05"
//        request.partnerId = "1526397881"
//        request.prepayId = prepayId
//        request.packageValue ="Sign=WXPay"
//        request.nonceStr = nonceStr
//        request.timeStamp = timestamp
//        request.sign = sign
//        request.extData = "app data"
//
//        api.sendReq(request)

//        api = WXAPIFactory.createWXAPI(this,"wxb4ba3c02aa476ea1")
//        api.registerApp("wxb4ba3c02aa476ea1")

//        var req = PayReq()
//        req.appId = "wxb4ba3c02aa476ea1"
//        req.partnerId = "1900006771"
//        req.prepayId = "wx26134232987428d8d79267b91735270639"
//        req.nonceStr = "209c7f47d8b9abe33ce18fbf8739501e"
//        req.timeStamp = "1553578953"
//        req.packageValue = "Sign=WXPay"
//        req.sign = "A4A9BF87023D975BDB59DB2C35B80EF1"
//        req.extData = "app data"
//        api.sendReq(req)


//        LogCat.e("CNM" + req.checkArgs())
//        LogCat.e("appId : " + "wxac8cb0c3f9b06b05" + " partnerId: " + "1526397881" + " prepayId: $prepayId" + " nonceStr: $nonceStr"
//                + " timestamp:  " + "Sign=WXPay" + " sign: $sign")

    }

}