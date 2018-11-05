package com.zxcx.zhizhe.ui.my.userInfo.userSafety

import android.content.Intent
import android.os.Bundle
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.utils.WechatClientNotExistException
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.RemoveBindingEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_user_safety.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 用户第三方账号绑定页面
 */

class UserSafetyActivity : MvpActivity<UserSafetyPresenter>(), UserSafetyContract.View {

	private var isBindingPhone: Boolean = false
	private var isBindingWechat: Boolean = false
	private var isBindingQQ: Boolean = false
	private var isBindingWeibo: Boolean = false
	private var channelType: Int = 0 // 1-QQ 2-WeChat 3-Weibo
	private var mChannelLoginListener: PlatformActionListener = ChannelLoginListener()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_user_safety)
		EventBus.getDefault().register(this)

		initToolBar("绑定")

		isBindingPhone = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingPhone, false)
		isBindingWechat = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWX, false)
		isBindingQQ = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingQQ, false)
		isBindingWeibo = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWB, false)

		updateView()
	}

	override fun onDestroy() {
		super.onDestroy()
		EventBus.getDefault().unregister(this)
	}

	override fun createPresenter(): UserSafetyPresenter {
		return UserSafetyPresenter(this)
	}

	override fun postSuccess() {
		when (channelType) {
			1 -> isBindingQQ = !isBindingQQ
			2 -> isBindingWechat = !isBindingWechat
			3 -> isBindingWeibo = !isBindingWeibo
		}
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingWX, isBindingWechat)
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingQQ, isBindingQQ)
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingWB, isBindingWeibo)
		updateView()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: RemoveBindingEvent) {
        LogCat.e("解绑")
		mPresenter.channelBinding(channelType, 2, null)
	}

	override fun setListener() {
		ll_user_safety_phone_bind.setOnClickListener {
			//更换手机界面
			val intent = Intent(this, ChangePhoneActivity::class.java)
			startActivity(intent)
		}

		ll_user_safety_wechat_bind.setOnClickListener {
			if (isBindingWechat) {
				//解绑微信
				channelType = 2
				val dialog = RemoveBindingDialog()
				val bundle = Bundle()
				bundle.putString("channel", "微信")
				dialog.arguments = bundle
				dialog.show(supportFragmentManager, "")
			} else {
				//绑定微信
				channelType = 2
				val wechat = ShareSDK.getPlatform(Wechat.NAME)
				wechat.platformActionListener = mChannelLoginListener
				wechat.showUser(null)
			}
		}

		ll_user_safety_qq_bind.setOnClickListener {
			if (isBindingQQ) {
				//解绑QQ
				channelType = 1
				val dialog = RemoveBindingDialog()
				val bundle = Bundle()
				bundle.putString("channel", "QQ")
				dialog.arguments = bundle
				dialog.show(supportFragmentManager, "")
			} else {
				//绑定QQ
				channelType = 1
				val qq = ShareSDK.getPlatform(QQ.NAME)
				qq.platformActionListener = mChannelLoginListener
				qq.showUser(null)
			}
		}

		ll_user_safety_weibo_bind.setOnClickListener {
			if (isBindingWeibo) {
				//解绑微博
				channelType = 3
				val dialog = RemoveBindingDialog()
				val bundle = Bundle()
				bundle.putString("channel", "微博")
				dialog.arguments = bundle
				dialog.show(supportFragmentManager, "")
			} else {
				//绑定微博
				channelType = 3
				val weibo = ShareSDK.getPlatform(SinaWeibo.NAME)
				weibo.platformActionListener = mChannelLoginListener
				weibo.showUser(null)
			}
		}
	}

	private fun updateView() {
		tv_user_safety_qq_bind_status.text = if (isBindingQQ) getString(R.string.is_binding) else getString(R.string.no_binding)
		tv_user_safety_wechat_bind_status.text = if (isBindingWechat) getString(R.string.is_binding) else getString(R.string.no_binding)
		tv_user_safety_weibo_bind_status.text = if (isBindingWeibo) getString(R.string.is_binding) else getString(R.string.no_binding)
		tv_user_safety_qq_bind_status.setTextColor(mActivity.getColorForKotlin(if (isBindingQQ) R.color.button_blue else R.color.text_color_3))
		tv_user_safety_wechat_bind_status.setTextColor(mActivity.getColorForKotlin(if (isBindingWechat) R.color.button_blue else R.color.text_color_3))
		tv_user_safety_weibo_bind_status.setTextColor(mActivity.getColorForKotlin(if (isBindingWeibo) R.color.button_blue else R.color.text_color_3))
	}

	internal inner class ChannelLoginListener : PlatformActionListener {

		override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
			if (i == Platform.ACTION_USER_INFOR) {
				val platDB = platform.db//获取数平台数据DB
				//通过DB获取各种数据
				val userId = platDB.userId
                LogCat.e("解除绑定前ID"+userId)
				mPresenter.channelBinding(channelType, 1, userId)
			}
		}

		override fun onError(platform: Platform, i: Int, throwable: Throwable) {
			throwable.printStackTrace()
			runOnUiThread {
				if (throwable is WechatClientNotExistException) {
					toastShow("请先安装微信客户端")
				} else {
					toastShow("授权失败")
				}
			}
		}

		override fun onCancel(platform: Platform, i: Int) {
			runOnUiThread { toastShow("授权取消") }
		}
	}
}
