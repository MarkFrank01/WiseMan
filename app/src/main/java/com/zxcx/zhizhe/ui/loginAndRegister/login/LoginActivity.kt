package com.zxcx.zhizhe.ui.loginAndRegister.login

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.View
import butterknife.ButterKnife
import cn.jiguang.analytics.android.api.JAnalyticsInterface
import cn.jpush.android.api.JPushInterface
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.utils.WechatClientNotExistException
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.google.gson.JsonParser
import com.jakewharton.rxbinding2.view.RxView
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.channelRegister.ChannelRegisterActivity
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * 登录页面
 */

class LoginActivity : MvpActivity<LoginPresenter>(), LoginContract.View {

	private var phoneRules = "^1\\d{10}$"
	private var phonePattern = Pattern.compile(phoneRules)

	private var mChannelLoginListener: PlatformActionListener = ChannelLoginListener()
	private var channelType: Int = 0 // 1-QQ 2-WeChat 3-Weibo
	private var appType: Int = 0
	private var userName: String? = null
	private var userId: String? = null
	private var userIcon: String? = null
	private var userGender: String? = null
	private var appChannel: String? = null
	private var appVersion: String? = null
	private var jpushID: String? = null
	private var verifyKey: String? = null

	val countDownTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
		override fun onFinish() {
			tv_login_resend_code.isEnabled = true
			tv_login_resend_code.text = "重新获取验证码"
		}

		override fun onTick(millisUntilFinished: Long) {
			val value = (millisUntilFinished / 1000).toInt()
			tv_login_resend_code.text = getString(R.string.tv_login_count_down, value)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		val uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
				View.SYSTEM_UI_FLAG_FULLSCREEN or
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		window.decorView.systemUiVisibility = uiFlags
		ButterKnife.bind(this)
		EventBus.getDefault().register(this)


		val str = "注册智者代表你已阅读并同意<font color='#0088AA'>《智者用户协议》</font>";
		tv_login_agreement.setText(Html.fromHtml(str));

		appType = Constants.APP_TYPE
		appChannel = WalleChannelReader.getChannel(mActivity)
		if (appChannel == null) {
			appChannel = "官方"
		}
		appVersion = Utils.getAppVersionName(mActivity)
		jpushID = JPushInterface.getRegistrationID(mActivity)
		SMSSDK.registerEventHandler(EventHandle())
	}

	override fun initStatusBar() {
		//全屏输入法问题
		AndroidBug5497Workaround.assistActivity(this)
	}

	override fun onBackPressed() {
		Utils.closeInputMethod(this)
		super.onBackPressed()
		countDownTimer.cancel()
	}

	public override fun onDestroy() {
		SMSSDK.unregisterAllEventHandler()
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun createPresenter(): LoginPresenter {
		return LoginPresenter(this)
	}

	override fun getDataSuccess(bean: LoginBean) {
		if (!bean.user.hasAttention) {
//			SharedPreferencesUtil.saveData(SVTSConstants.isFirstLogin, false)
			startActivity(SelectAttentionActivity::class.java) {}
		}
		ZhiZheUtils.saveLoginData(bean)
		//极光统计
		val lEvent = cn.jiguang.analytics.android.api.LoginEvent("defult", true)
		lEvent.addKeyValue("appChannel", WalleChannelReader.getChannel(mActivity)).addKeyValue("appVersion", Utils.getAppVersionName(mActivity))
		JAnalyticsInterface.onEvent(mActivity, lEvent)
		//登录成功通知
		EventBus.getDefault().post(LoginEvent())
		Utils.closeInputMethod(mActivity)
		toastShow("欢迎来到智者")
		finish()
	}

	override fun channelLoginSuccess(bean: LoginBean) {
		getDataSuccess(bean)
	}

	override fun channelLoginNeedRegister() {
		mActivity.startActivity(ChannelRegisterActivity::class.java) {
			it.putExtra("userId", userId)
			it.putExtra("userName", userName)
			it.putExtra("userIcon", userIcon)
			it.putExtra("userGender", userGender)
			it.putExtra("channelType", channelType)
		}
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: PhoneConfirmEvent) {
		//手机号确认成功,发送验证码
		showLoading()
		SMSSDK.getVerificationCode("86", et_login_phone.text.toString())
	}

	private fun checkPhone(): Boolean {
		return phonePattern.matcher(et_login_phone.text.toString()).matches()
	}

	override fun setListener() {
		iv_login_close.setOnClickListener { onBackPressed() }

		tv_login_phone.setOnClickListener {
			rl_login_phone.visibility = View.VISIBLE
			ll_login_code.visibility = View.GONE
			Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
				et_login_phone.requestFocus()
			}
		}

		tv_login_send_code.setOnClickListener {
			val confirmDialog = PhoneConfirmDialog()
			val bundle = Bundle()
			bundle.putString("phone", et_login_phone.text.toString())
			confirmDialog.arguments = bundle
			confirmDialog.show(mActivity.supportFragmentManager, "")
		}

		tv_login_resend_code.setOnClickListener {
			SMSSDK.getVerificationCode("86", et_login_phone.text.toString())
		}

		iv_login_phone_clear.setOnClickListener {
			et_login_phone.setText("")
			Utils.showInputMethod(et_login_phone)
		}

		RxView.clicks(iv_login_qq).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe {
			channelType = 1
			val qq = ShareSDK.getPlatform(QQ.NAME)
			qq.platformActionListener = mChannelLoginListener
			qq.showUser(null)
		}

		RxView.clicks(iv_login_wechat).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe {
			channelType = 2
			val wechat = ShareSDK.getPlatform(Wechat.NAME)
			wechat.platformActionListener = mChannelLoginListener
			wechat.showUser(null)
		}

		RxView.clicks(iv_login_weibo).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe {
			channelType = 3
			val weibo = ShareSDK.getPlatform(SinaWeibo.NAME)
			weibo.platformActionListener = mChannelLoginListener
			weibo.showUser(null)
		}

		et_login_phone.afterTextChanged {
			val isPhone = checkPhone()
			if (isPhone) {
				Utils.closeInputMethod(et_login_phone)
				et_login_phone.clearFocus()
			}
			iv_login_phone_clear.visibility = if (isPhone || et_login_phone.length() == 0) View.GONE else View.VISIBLE
			tv_login_send_code.isEnabled = isPhone
		}

		vci_login.setOnCompleteListener {
			Utils.closeInputMethod(et_login_phone)
			mPresenter.smsCodeLogin(et_login_phone.text.toString(), it, jpushID, appType, appChannel, appVersion)
		}

		tv_login_agreement.setOnClickListener {
			startActivity(WebViewActivity::class.java) {
				it.putExtra("title", getString(R.string.agreement))
				if (Constants.IS_NIGHT) {
					it.putExtra("url", getString(R.string.base_url) + getString(R.string.agreement_dark_url))
				} else {
					it.putExtra("url", getString(R.string.base_url) + getString(R.string.agreement_url))
				}
			}
		}
	}

	internal inner class ChannelLoginListener : PlatformActionListener {

		override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
			if (i == Platform.ACTION_USER_INFOR) {
				val platDB = platform.db//获取数平台数据DB
				//通过DB获取各种数据
				userGender = platDB.userGender
				userIcon = platDB.userIcon
				userId = platDB.userId
				userName = platDB.userName

                val Uid = platDB.get("unionid")
                LogCat.e("UID" + Uid)
                LogCat.e("登录前ID"+userId)
				mPresenter.channelLogin(channelType, Uid, jpushID, appType, appChannel, appVersion)
			}
		}

		override fun onError(platform: Platform, i: Int, throwable: Throwable) {
			throwable.printStackTrace()
			mActivity.runOnUiThread {
				if (throwable is WechatClientNotExistException) {
					toastShow("请先安装微信客户端")
				} else {
					toastShow("登录失败")
				}
			}
		}

		override fun onCancel(platform: Platform, i: Int) {
			mActivity.runOnUiThread { toastShow("登录取消") }
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
							tv_login_phone.text = et_login_phone.text.toString()
							rl_login_phone.visibility = View.GONE
							ll_login_code.visibility = View.VISIBLE
							tv_login_resend_code.isEnabled = false
							Observable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe {
								vci_login.focus()
							}
							countDownTimer.start()
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
