package com.zxcx.zhizhe.pay.wx

import android.os.Bundle
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.Constants
import kotlinx.android.synthetic.main.activity_entry_wxpay.*

/**
 * @author : MarkFrank01
 * @Created on 2019/2/23
 * @Description :
 */
class WXEntryActivity : BaseActivity() {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_wxpay)
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)

        api.registerApp(Constants.WX_APP_ID)

        goto_send_btn.setOnClickListener {
            goto_send_btn.isEnabled = false

            //待服务器那边处理
            //此均为测试写法
            var req = PayReq()
            req.appId = Constants.WX_APP_ID
            req.partnerId = "1900006771"
            req.prepayId = "Sign=WXPay"
            req.nonceStr = "18844964480834c91cef8b15e56b0a4d"
            req.timeStamp = "1550898756"
            req.packageValue = "Sign=WXPay"
            req.sign = "96C707EFD84B2EBE72889EBA07009EAD"
            req.extData = "app data" // optional
            api.sendReq(req)


            goto_send_btn.isEnabled = true
        }
    }
}