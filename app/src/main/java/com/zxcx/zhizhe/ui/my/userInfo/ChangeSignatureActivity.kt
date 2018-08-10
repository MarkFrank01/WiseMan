package com.zxcx.zhizhe.ui.my.userInfo

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_change_signature.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by anm on 2017/7/13.
 * 修改签名页面
 */

class ChangeSignatureActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_signature)

		initToolBar()
		tv_toolbar_right.visibility = View.VISIBLE
		tv_toolbar_right.text = "完成"
		tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

		val signature = SharedPreferencesUtil.getString(SVTSConstants.signature, "")
		et_dialog_change_signature.setText(signature)
		et_dialog_change_signature.setSelection(signature.length)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		Utils.closeInputMethod(et_dialog_change_signature)
	}

	override fun postSuccess(bean: UserInfoBean) {
		ZhiZheUtils.saveUserInfo(bean)
		toastShow(R.string.user_info_change)
		onBackPressed()
	}

	override fun postFail(msg: String) {
		toastFail(msg)
	}

	override fun setListener() {
		tv_toolbar_right.setOnClickListener {
			changeSignature(et_dialog_change_signature.text.toString())
		}

		et_dialog_change_signature.afterTextChanged {
			/*if (et_dialog_change_signature.length() in 1..5 || et_dialog_change_signature.length() == 18) {
				tv_change_signature_hint.visibility = View.VISIBLE
			} else {
				tv_change_signature_hint.visibility = View.GONE
			}*/
			tv_toolbar_right.isEnabled = it.length in 6..18
		}
	}

	private fun changeSignature(signature: String) {
		mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, null, signature)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main_loading(this))
				.subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
					override fun onNext(bean: UserInfoBean) {
						postSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}
}
