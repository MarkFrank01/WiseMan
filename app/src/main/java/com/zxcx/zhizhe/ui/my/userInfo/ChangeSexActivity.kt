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
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.ZhiZheUtils
import kotlinx.android.synthetic.main.activity_change_sex.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by anm on 2017/7/13.
 */

class ChangeSexActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

	private var sex: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_sex)

		initToolBar()
		tv_toolbar_right.visibility = View.VISIBLE
		tv_toolbar_right.text = "完成"
		tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

		sex = SharedPreferencesUtil.getInt(SVTSConstants.sex, 1)
		rb_change_sex_man.isChecked = sex == 1
		rb_change_sex_woman.isChecked = sex == 0
	}

	override fun setListener() {

		tv_toolbar_right.setOnClickListener {
			sex = if (rb_change_sex_man.isChecked) 1 else 0
			changeSex(sex)
		}
	}

	private fun changeSex(sex: Int) {
		mDisposable = AppClient.getAPIService().changeUserInfo(null, null, sex, null, null)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleResult())
				.subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
					override fun onNext(bean: UserInfoBean) {
						postSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	override fun postSuccess(bean: UserInfoBean) {
		ZhiZheUtils.saveUserInfo(bean)
		toastShow(R.string.user_info_change)
		onBackPressed()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}
}
