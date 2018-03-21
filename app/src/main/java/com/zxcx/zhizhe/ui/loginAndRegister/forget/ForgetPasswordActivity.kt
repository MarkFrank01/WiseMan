package com.zxcx.zhizhe.ui.loginAndRegister.forget

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import butterknife.ButterKnife
import cn.jpush.android.api.JPushInterface
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.google.gson.JsonParser
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.event.RegisterEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean
import com.zxcx.zhizhe.ui.loginAndRegister.register.PhoneConfirmDialog
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_forget_password.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ForgetPasswordActivity : MvpActivity<ForgetPasswordPresenter>(), ForgetPasswordContract.View {

    private var phoneRules = "^1\\d{10}$"
    private var passwordRules = "^.{8,20}$"
    private var phonePattern = Pattern.compile(phoneRules)
    private var passwordPattern = Pattern.compile(passwordRules)
    private var verifyKey: String? = null
    private var jpushRID: String? = null

    val countDownTimer: CountDownTimer = object : CountDownTimer(60000,1000){
        override fun onFinish() {
            tv_forget_resend_code.isEnabled = true
            tv_forget_resend_code.text = "重新获取验证码"
        }

        override fun onTick(millisUntilFinished: Long) {
            val value = (millisUntilFinished / 1000).toInt()
            tv_forget_resend_code.text = getString(R.string.tv_login_count_down,value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)

        jpushRID = JPushInterface.getRegistrationID(mActivity)
        SMSSDK.registerEventHandler(EventHandle())
        Observable.timer(300,TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread())
                .subscribe { Utils.showInputMethod(et_forget_phone) }
    }

    override fun initStatusBar() {

    }

    public override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
        SMSSDK.unregisterAllEventHandler()
    }

    override fun createPresenter(): ForgetPasswordPresenter {
        return ForgetPasswordPresenter(this)
    }

    override fun getDataSuccess(bean: LoginBean) {
        ZhiZheUtils.saveLoginData(bean)
        EventBus.getDefault().post(RegisterEvent())
        finish()
    }

    override fun smsCodeVerificationSuccess(bean: SMSCodeVerificationBean) {
        tv_forget_confirm.isEnabled = true
        verifyKey = bean.verifyKey
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PhoneConfirmEvent) {
        //手机号确认成功,发送验证码
        showLoading()
        SMSSDK.getVerificationCode("86", et_forget_phone.text.toString())
    }

    private fun checkPhone(): Boolean {
        return phonePattern.matcher(et_forget_phone.text.toString()).matches()
    }

    private fun checkPassword(): Boolean {
        return passwordPattern.matcher(et_forget_password!!.text.toString()).matches()
    }

    override fun setListener() {
        iv_forget_close.setOnClickListener { onBackPressed() }

        iv_forget_phone_clear.setOnClickListener { et_forget_phone.setText("") }

        tv_forget_phone.setOnClickListener {
            rl_forget_phone.visibility = View.VISIBLE
            ll_forget_code.visibility = View.GONE
            Observable.timer(300,TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe{
                et_forget_phone.requestFocus()
            }
        }

        tv_forget_password.setOnClickListener {
            rl_forget_phone.visibility = View.VISIBLE
            ll_forget_code.visibility = View.GONE
            Observable.timer(300,TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe{
                et_forget_password.requestFocus()
            }
        }

        tv_forget_send_code.setOnClickListener {
            val confirmDialog = PhoneConfirmDialog()
            val bundle = Bundle()
            bundle.putString("phone", et_forget_phone.text.toString())
            confirmDialog.arguments = bundle
            confirmDialog.show(mActivity.fragmentManager, "")
        }

        et_forget_phone.afterTextChanged {
            val isPhone = checkPhone()
            iv_forget_phone_clear.visibility = if (et_forget_phone.length() == 0) View.GONE else View.VISIBLE
            tv_forget_send_code.isEnabled = isPhone && checkPassword()
            et_forget_password.isEnabled = isPhone
        }

        et_forget_password.afterTextChanged {
            cb_forget_password_show.visibility = if (et_forget_password.length() == 0) View.GONE else View.VISIBLE
            tv_forget_send_code.isEnabled = checkPhone() && checkPassword()
        }

        cb_forget_password_show.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                et_forget_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                et_forget_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        vci_forget.setOnCompleteListener {
            mPresenter.smsCodeVerification(et_forget_phone.text.toString(),it)
        }

        tv_forget_confirm.setOnClickListener {
            val jpushRID = JPushInterface.getRegistrationID(mActivity)
            val appType = Constants.APP_TYPE
            val password = MD5Utils.md5(et_forget_password.text.toString())
            mPresenter.forgetPassword(et_forget_phone.text.toString(), verifyKey, jpushRID, password, appType)
        }
    }

    internal inner class EventHandle : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any) {
            mActivity.runOnUiThread {
                hideLoading()
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    when (event) {
                        SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> { /*提交验证码成功*/ }
                        SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
                            /*获取验证码成功*/
                            toastShow(R.string.send_verification_code_success)
                            tv_forget_phone.text = et_forget_phone.text.toString()
                            rl_forget_phone.visibility = View.GONE
                            ll_forget_code.visibility = View.VISIBLE
                            tv_forget_resend_code.isEnabled = false
                            Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe{
                                vci_forget.focus()
                            }
                            countDownTimer.start()
                        }
                        SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES -> {/*返回支持发送验证码的国家列表*/ }
                    }
                } else {
                    // 根据服务器返回的网络错误，给toast提示
                    try {
                        val throwable = data as Throwable
                        throwable.printStackTrace()
                        val `object` = JsonParser().parse(throwable.message).asJsonObject
                        val des = `object`.get("detail").asString//错误描述
                        val status = `object`.get("status").asInt//错误代码
                        when (status) {
                            457 -> toastShow("手机号格式错误!")
                            463, 464, 465, 477, 478 -> toastShow("获取验证码次数频繁，请稍后重试")
                            else -> toastShow(des)
                        }
                    } catch (e: Exception) {
                        //do something
                    }

                }
            }
        }
    }
}
