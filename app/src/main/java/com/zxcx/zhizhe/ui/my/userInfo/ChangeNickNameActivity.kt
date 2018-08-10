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
import kotlinx.android.synthetic.main.activity_change_nick_name.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by anm on 2017/7/13.
 * 修改昵称页面
 */

class ChangeNickNameActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

	private var name: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_nick_name)

		initToolBar()
		tv_toolbar_right.visibility = View.VISIBLE
		tv_toolbar_right.text = "完成"
		tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

		name = SharedPreferencesUtil.getString(SVTSConstants.nickName, "")
		et_dialog_change_nick_name.setText(name)
		et_dialog_change_nick_name.setSelection(name?.length ?: 0)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		Utils.closeInputMethod(et_dialog_change_nick_name)
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
			if (et_dialog_change_nick_name.length() < 2) {
				return@setOnClickListener
			}
			if (et_dialog_change_nick_name.text.toString() != name) {
				changeNickName(et_dialog_change_nick_name.text.toString())
			} else {
				toastShow(R.string.user_info_change)
				onBackPressed()
			}
		}

		et_dialog_change_nick_name.afterTextChanged {
			tv_toolbar_right.isEnabled = et_dialog_change_nick_name.length() > 1
		}
	}

	private fun changeNickName(name: String) {
		mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null, null)
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
