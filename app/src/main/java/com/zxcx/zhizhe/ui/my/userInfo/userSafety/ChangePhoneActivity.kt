package com.zxcx.zhizhe.ui.my.userInfo.userSafety

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.google.gson.JsonParser
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.channelRegister.ChannelRegisterContract
import com.zxcx.zhizhe.ui.loginAndRegister.channelRegister.ChannelRegisterPresenter
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean
import com.zxcx.zhizhe.ui.loginAndRegister.login.PhoneConfirmDialog
import com.zxcx.zhizhe.ui.loginAndRegister.login.SMSCodeVerificationBean
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_change_phone.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ChangePhoneActivity : MvpActivity<ChannelRegisterPresenter>(), ChannelRegisterContract.View {

	internal var phoneRules = "^1\\d{10}$"
	internal var phonePattern = Pattern.compile(phoneRules)
	private var phone: String? = null
	private var verifyKey: String? = null
	private var newPhone: String? = null

	val countDownTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
		override fun onFinish() {
			tv_change_phone_resend_code.isEnabled = true
			tv_change_phone_resend_code.text = "重新获取验证码"
		}

		override fun onTick(millisUntilFinished: Long) {
			val value = (millisUntilFinished / 1000).toInt()
			tv_change_phone_resend_code.text = getString(R.string.tv_login_count_down, value)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_phone)
		EventBus.getDefault().register(this)
		SMSSDK.registerEventHandler(EventHandle())
	}

	override fun initStatusBar() {
		mImmersionBar = ImmersionBar.with(this).keyboardEnable(true)
		if (!Constants.IS_NIGHT) {
			mImmersionBar
					.statusBarColor(R.color.background)
					.statusBarDarkFont(true, 0.2f)
					.flymeOSStatusBarFontColor(R.color.text_color_1)
					.fitsSystemWindows(true)
					.keyboardEnable(true)
		} else {
			mImmersionBar
					.statusBarColor(R.color.background)
					.flymeOSStatusBarFontColor(R.color.text_color_1)
					.fitsSystemWindows(true)
					.keyboardEnable(true)
		}
		mImmersionBar.init()
	}

	override fun onBackPressed() {
		Utils.closeInputMethod(this)
		EventBus.getDefault().unregister(this)
		super.onBackPressed()
		countDownTimer.cancel()
	}

	public override fun onDestroy() {
		SMSSDK.unregisterAllEventHandler()
		super.onDestroy()
	}

	override fun createPresenter(): ChannelRegisterPresenter {
		return ChannelRegisterPresenter(this)
	}

	override fun getDataSuccess(bean: LoginBean) {
		SharedPreferencesUtil.saveData(SVTSConstants.phone, newPhone)
		finish()
	}

	fun smsCodeVerificationSuccess(bean: SMSCodeVerificationBean) {
		//验证码验证成功
		verifyKey = bean.verifyKey
		rl_change_phone_phone.visibility = View.VISIBLE
		ll_change_phone_code.visibility = View.GONE
		et_change_phone_phone.isEnabled = true
		et_change_phone_phone.setText("")
		et_change_phone_phone.setTextColor(getColorForKotlin(R.color.text_color_1))
		tv_change_phone_phone.setTextColor(getColorForKotlin(R.color.text_color_1))
		countDownTimer.onFinish()
		Utils.showInputMethod(et_change_phone_phone)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: PhoneConfirmEvent) {
		//手机号确认成功,发送验证码
		showLoading()
		SMSSDK.getVerificationCode("86", et_change_phone_phone.text.toString())
	}

	override fun setListener() {
		iv_change_phone_close.setOnClickListener { onBackPressed() }

		tv_change_phone_phone.setOnClickListener {
			if (newPhone.isNullOrEmpty())
				return@setOnClickListener
			rl_change_phone_phone.visibility = View.VISIBLE
			ll_change_phone_code.visibility = View.GONE
			Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
				et_change_phone_phone.requestFocus()
			}
		}

		tv_change_phone_send_code.setOnClickListener {
			if (newPhone.isNullOrEmpty()) {
				showLoading()
				SMSSDK.getVerificationCode("86", et_change_phone_phone.text.toString())
			} else {
				val confirmDialog = PhoneConfirmDialog()
				val bundle = Bundle()
				bundle.putString("phone", et_change_phone_phone.text.toString())
				confirmDialog.arguments = bundle
				confirmDialog.show(mActivity.fragmentManager, "")
			}
		}

		tv_change_phone_resend_code.setOnClickListener {
			SMSSDK.getVerificationCode("86", et_change_phone_phone.text.toString())
		}

		iv_change_phone_phone_clear.setOnClickListener {
			et_change_phone_phone.setText("")
			Utils.showInputMethod(et_change_phone_phone)
		}

		//在添加监听前初始化手机号
		phone = SharedPreferencesUtil.getString(SVTSConstants.phone, "")
		et_change_phone_phone.setText(phone)
		tv_change_phone_send_code.isEnabled = true
		et_change_phone_phone.afterTextChanged {
			val isPhone = checkPhone()
			if (isPhone) {
				Utils.closeInputMethod(et_change_phone_phone)
				et_change_phone_phone.clearFocus()
				newPhone = et_change_phone_phone.text.toString()
			}
			iv_change_phone_phone_clear.visibility = if (isPhone || et_change_phone_phone.length() == 0) View.GONE else View.VISIBLE
			tv_change_phone_send_code.isEnabled = isPhone
		}

		vci_change_phone.setOnCompleteListener {
			Utils.closeInputMethod(et_change_phone_phone)
			if (newPhone.isNullOrEmpty()) {
				//mPresenter.smsCodeVerification(et_change_phone_phone.text.toString(), it)
			} else {
				SMSSDK.submitVerificationCode("86", newPhone, it)
			}
		}
	}

	private fun checkPhone(): Boolean {
		return phonePattern.matcher(et_change_phone_phone.text.toString()).matches()
	}

	internal inner class EventHandle : EventHandler() {
		override fun afterEvent(event: Int, result: Int, data: Any?) {
			mActivity.runOnUiThread {
				hideLoading()
				if (result == SMSSDK.RESULT_COMPLETE) {
					//回调完成
					when (event) {
						SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> //提交验证码成功
							mPresenter.changePhone(newPhone, verifyKey)
						SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
							//获取验证码成功
							toastShow(R.string.send_verification_code_success)
							tv_change_phone_phone.text = et_change_phone_phone.text.toString()
							rl_change_phone_phone.visibility = View.GONE
							ll_change_phone_code.visibility = View.VISIBLE
							tv_change_phone_resend_code.isEnabled = false
							Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
								vci_change_phone.focus()
							}
							countDownTimer.start()
						}
						SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES -> {
							//返回支持发送验证码的国家列表
						}
					}
				} else {
					// 根据服务器返回的网络错误，给toast提示
					try {
						val throwable = data as Throwable?
						throwable!!.printStackTrace()
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
