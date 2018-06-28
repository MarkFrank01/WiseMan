package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import butterknife.ButterKnife
import cn.jpush.android.api.JPushInterface
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.google.gson.JsonParser
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.event.StopRegisteredEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean
import com.zxcx.zhizhe.ui.loginAndRegister.login.PhoneConfirmDialog
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_channel_register.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ChannelRegisterActivity : MvpActivity<ChannelRegisterPresenter>(), ChannelRegisterContract.View {

	private var phoneRules = "^1\\d{10}$"
	private var phonePattern = Pattern.compile(phoneRules)
	private var jpushRID: String? = null
	private var userId: String? = null
	private var userName: String? = null
	private var userIcon: String? = null
	private var userGender: String? = null
	private var appChannel: String? = null
	private var appVersion: String? = null
	private var channelType: Int = 0
	private var appType: Int = 0
	private var sex: Int = 0

	val countDownTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
		override fun onFinish() {
			tv_forget_resend_code.isEnabled = true
			tv_forget_resend_code.text = "重新获取验证码"
		}

		override fun onTick(millisUntilFinished: Long) {
			val value = (millisUntilFinished / 1000).toInt()
			tv_forget_resend_code.text = getString(R.string.tv_login_count_down, value)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_channel_register)
		ButterKnife.bind(this)
		EventBus.getDefault().register(this)

		jpushRID = JPushInterface.getRegistrationID(mActivity)
		SMSSDK.registerEventHandler(EventHandle())
		initData()
		//延迟弹出软键盘
		Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
				.subscribe { Utils.showInputMethod(et_forget_phone) }
	}

	private fun initData() {

		appType = Constants.APP_TYPE
		appChannel = WalleChannelReader.getChannel(this)
		appVersion = Utils.getAppVersionName(this)
		val intent = intent
		userId = intent.getStringExtra("userId")
		userName = intent.getStringExtra("userName")
		userIcon = intent.getStringExtra("userIcon")
		userGender = intent.getStringExtra("userGender")
		channelType = intent.getIntExtra("channelType", 1)

		sex = if ("m" == userGender) 1 else 2
	}

	override fun initStatusBar() {
		//全屏输入法问题
		AndroidBug5497Workaround.assistActivity(this)
	}

	override fun onBackPressed() {
		val stopRegisteredDialog = StopRegisteredDialog()
		stopRegisteredDialog.show(mActivity.fragmentManager, "")
	}

	public override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
		SMSSDK.unregisterAllEventHandler()
	}

	override fun createPresenter(): ChannelRegisterPresenter {
		return ChannelRegisterPresenter(this)
	}

	override fun getDataSuccess(bean: LoginBean) {
		ZhiZheUtils.saveLoginData(bean)
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: PhoneConfirmEvent) {
		//手机号确认成功,发送验证码
		showLoading()
		SMSSDK.getVerificationCode("86", et_forget_phone.text.toString())
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: StopRegisteredEvent) {
		finish()
	}

	private fun checkPhone(): Boolean {
		return phonePattern.matcher(et_forget_phone.text.toString()).matches()
	}

	override fun setListener() {
		iv_forget_close.setOnClickListener { onBackPressed() }

		iv_forget_phone_clear.setOnClickListener {
			et_forget_phone.setText("")
			Utils.showInputMethod(et_forget_phone)
		}

		tv_forget_phone.setOnClickListener {
			rl_forget_phone.visibility = View.VISIBLE
			ll_forget_code.visibility = View.GONE
			Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
				et_forget_phone.requestFocus()
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
			tv_forget_send_code.isEnabled = isPhone
		}

		vci_forget.setOnCompleteListener {
			val jpushRID = JPushInterface.getRegistrationID(mActivity)
			val appType = Constants.APP_TYPE
			val phone = et_forget_phone.text.toString()
			mPresenter.channelRegister(channelType, userId, userIcon, userName, sex,
					null, phone, it, jpushRID, appType, appChannel, appVersion)
		}
	}

	internal inner class EventHandle : EventHandler() {
		override fun afterEvent(event: Int, result: Int, data: Any) {
			mActivity.runOnUiThread {
				hideLoading()
				if (result == SMSSDK.RESULT_COMPLETE) {
					//回调完成
					when (event) {
						SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> { /*提交验证码成功*/
						}
						SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
							/*获取验证码成功*/
							toastShow(R.string.send_verification_code_success)
							tv_forget_phone.text = et_forget_phone.text.toString()
							rl_forget_phone.visibility = View.GONE
							ll_forget_code.visibility = View.VISIBLE
							tv_forget_resend_code.isEnabled = false
							Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
								vci_forget.focus()
							}
							countDownTimer.start()
						}
						SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES -> {/*返回支持发送验证码的国家列表*/
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
