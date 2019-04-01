package com.zxcx.zhizhe.pay.zfb

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/2/23
 * @Description :
 */
//class ZFBEntryActivity : BaseActivity() {
class ZFBEntryActivity : MvpActivity<ZFBPresenter>(),ZFBContract.View {


    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    val APPID = "2019010862850729"

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    val PID = ""

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    val TARGET_ID = ""

    /**
     * pkcs8 格式的商户私钥。
     *
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     *
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    val RSA2_PRIVATE = ""
    var RSA_PRIVATE = ""

    /**
     * 获取权限使用的RequestCode
     */
    private val PERMISSIONS_REQUEST_CODE = 1002

    private val SDK_PAY_FLAG = 1
    private val SDK_AUTH_FLAG = 2

    private var circleId = 0

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    val resultStatus = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(this@PayDemoActivity, getString(R.string.pay_success) + payResult)
                        toastShow("成功哦")
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(this@PayDemoActivity, getString(R.string.pay_failed) + payResult)
                        toastShow("失败")
                    }
                }
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String, String>, true)
                    val resultStatus = authResult.getResultStatus()

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        showAlert(this@PayDemoActivity, getString(R.string.auth_success) + authResult)
                        toastShow("授权成功")
                    } else {
                        // 其他状态值则为授权失败
//                        showAlert(this@PayDemoActivity, getString(R.string.auth_failed) + authResult)
                        toastShow("授权失败")
                    }

                }
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_zfbpay)
        requestPermission()

        initData()

        mPresenter.getAlOrderPayForJoinCircle(circleId)
    }

    fun initData(){
        circleId = intent.getIntExtra("circleId",0)
    }

    private fun open(){
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            toastShow("错误: 需要配置 PayDemoActivity 中的 APPID 和 RSA_PRIVATE")
            return
        }

        LogCat.e("appId is "+APPID)
        LogCat.e("RSA is "+RSA_PRIVATE)

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
//        var rsa2: Boolean = (RSA2_PRIVATE.length > 0)
//        var params: Map<String, String> = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2)
//        var orderParam: String = OrderInfoUtil2_0.buildOrderParam(params)
//
//        val privateKey = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
//        var sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2)
//        val orderInfo: String = orderParam + "&" + sign

        val payRunnable = Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2(RSA_PRIVATE, true)

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        //必须异步调用
        var payThread = Thread(payRunnable)
        payThread.start()

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_CODE)
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    PERMISSIONS_REQUEST_CODE)
//        } else {
//            toastShow("支付宝所需权限不足")
//        }
    }

    override fun createPresenter(): ZFBPresenter {
        return ZFBPresenter(this)
    }

    override fun getAlOrderPayForJoinCircle(key:String) {
        RSA_PRIVATE = key
        open()
    }


    override fun postSuccess(bean: ZFBBean?) {
    }

    override fun postFail(msg: String?) {
    }


    /**
     * 权限获取回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {

                //用户取消了权限弹窗
                if (grantResults.size == 0) {
                    toastShow("无法获取支付宝 SDK 所需的权限, 请到系统设置开启")
                    return
                }

                //用户拒绝了某些权限
                for (x in grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        toastShow("无法获取支付宝 SDK 所需的权限, 请到系统设置开启")
                        return
                    }
                }

                //所需的权限均正常获取
                toastShow("支付宝 SDK 所需的权限已经正常获取")
            }
        }
    }

//    fun payV2(v: View) {
//        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            toastShow("错误: 需要配置 PayDemoActivity 中的 APPID 和 RSA_PRIVATE")
//            return
//        }
//
//        /*
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * orderInfo 的获取必须来自服务端；
//         */
//        var rsa2: Boolean = (RSA2_PRIVATE.length > 0)
//        var params: Map<String, String> = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2)
//        var orderParam: String = OrderInfoUtil2_0.buildOrderParam(params)
//
//        val privateKey = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
//        var sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2)
//        val orderInfo: String = orderParam + "&" + sign
//
//        val payRunnable = Runnable {
//            val alipay = PayTask(this)
//            val result = alipay.payV2(orderInfo, true)
//
//            val msg = Message()
//            msg.what = SDK_PAY_FLAG
//            msg.obj = result
//            mHandler.sendMessage(msg)
//        }
//
//        //必须异步调用
//        var payThread = Thread(payRunnable)
//        payThread.start()
//    }
//
//    fun authV2(v: View) {
//        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
//                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
//                || TextUtils.isEmpty(TARGET_ID)) {
//            toastShow("错误: 需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
//            return
//        }
//        /*
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * authInfo 的获取必须来自服务端；
//         */
//
//        var rsa2:Boolean = (RSA2_PRIVATE.length>0)
//        var authInfoMap:Map<String,String> = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2)
//        var info = OrderInfoUtil2_0.buildOrderParam(authInfoMap)
//
//        var privatekey = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
//        var sign = OrderInfoUtil2_0.getSign(authInfoMap,privatekey,rsa2)
//        var authInfo:String = info +"&"+sign
//        val authRunnable = Runnable {
//            // 构造AuthTask 对象
//            val authTask = AuthTask(this)
//            // 调用授权接口，获取授权结果
//            val result = authTask.authV2(authInfo, true)
//
//            val msg = Message()
//            msg.what = SDK_AUTH_FLAG
//            msg.obj = result
//            mHandler.sendMessage(msg)
//        }
//
//        //必须异步调用
//        val authThread = Thread(authRunnable)
//        authThread.start()
//    }
}