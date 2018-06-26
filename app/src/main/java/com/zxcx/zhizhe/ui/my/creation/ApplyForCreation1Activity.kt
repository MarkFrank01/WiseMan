package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import butterknife.ButterKnife
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.google.gson.JsonParser
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.PhoneConfirmDialog
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import com.zxcx.zhizhe.utils.startActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_apply_for_creation_1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * 申请创作
 */
class ApplyForCreation1Activity : BaseActivity() {

    private var phoneRules = "^1\\d{10}$"
    private var phonePattern = Pattern.compile(phoneRules)

    val countDownTimer: CountDownTimer = object : CountDownTimer(60000,1000){
        override fun onFinish() {
            tv_afc_resend_code.isEnabled = true
            tv_afc_resend_code.text = "重新获取验证码"
        }

        override fun onTick(millisUntilFinished: Long) {
            val value = (millisUntilFinished / 1000).toInt()
            tv_afc_resend_code.text = getString(R.string.tv_login_count_down,value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_for_creation_1)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)
        SMSSDK.registerEventHandler(EventHandle())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        countDownTimer.cancel()
    }

    public override fun onDestroy() {
        SMSSDK.unregisterAllEventHandler()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PhoneConfirmEvent) {
        //手机号确认成功,发送验证码
        showLoading()
        SMSSDK.getVerificationCode("86", et_afc_phone.text.toString())
    }

    private fun checkPhone(): Boolean {
        return phonePattern.matcher(et_afc_phone.text.toString()).matches()
    }

    override fun setListener() {
        iv_afc_close.setOnClickListener { onBackPressed() }

        tv_afc_phone.setOnClickListener {
            rl_afc_phone.visibility = View.VISIBLE
            ll_afc_code.visibility = View.GONE
            Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe{
                et_afc_phone.requestFocus()
            }
        }

        tv_afc_send_code.setOnClickListener {
            val confirmDialog = PhoneConfirmDialog()
            val bundle = Bundle()
            bundle.putString("phone", et_afc_phone.text.toString())
            confirmDialog.arguments = bundle
            confirmDialog.show(mActivity.fragmentManager, "")
        }

        tv_afc_resend_code.setOnClickListener {
            SMSSDK.getVerificationCode("86", et_afc_phone.text.toString())
        }

        iv_afc_phone_clear.setOnClickListener {
            et_afc_phone.setText("")
            Utils.showInputMethod(et_afc_phone)
        }

        et_afc_phone.afterTextChanged {
            val isPhone = checkPhone()
            if (isPhone){
                Utils.closeInputMethod(et_afc_phone)
                et_afc_phone.clearFocus()
            }
            iv_afc_phone_clear.visibility = if (isPhone || et_afc_phone.length() == 0) View.GONE else View.VISIBLE
            tv_afc_send_code.isEnabled = isPhone
        }

        vci_afc.setOnCompleteListener {
            Utils.closeInputMethod(et_afc_phone)
            SMSSDK.submitVerificationCode("86",et_afc_phone.text.toString(),it)
        }
    }

    internal inner class EventHandle : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any) {
            mActivity.runOnUiThread {
                hideLoading()
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    when (event) {
                        SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
                            //获取验证码成功
                            toastShow(R.string.send_verification_code_success)
                            tv_afc_phone.text = et_afc_phone.text.toString()
                            rl_afc_phone.visibility = View.GONE
                            ll_afc_code.visibility = View.VISIBLE
                            tv_afc_resend_code.isEnabled = false
                            Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe{
                                vci_afc.focus()
                            }
                            countDownTimer.start()
                        }
                        SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                            //提交验证码成功
                            startActivity(ApplyForCreation2Activity::class.java,{
                                it.putExtra("phone",et_afc_phone.text.toString())
                            })
                            finish()
                        }
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
                            457 -> toastError("手机号格式错误!")
                            463, 464, 465, 477, 478 -> toastError("获取验证码次数频繁，请稍后重试")
                            else -> toastError(des)
                        }
                    } catch (e: Exception) {
                        //do something
                    }

                }
            }
        }
    }
}
