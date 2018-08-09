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
import kotlinx.android.synthetic.main.activity_change_birthday.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by anm on 2017/7/6.
 * 修改生日页面
 */

class ChangeBirthdayActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

	private var mBirth: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_birthday)

		initToolBar()
		tv_toolbar_right.visibility = View.VISIBLE
		tv_toolbar_right.text = "完成"
		tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

		mBirth = SharedPreferencesUtil.getString(SVTSConstants.birthday, "")
		if (StringUtils.isEmpty(mBirth)) {
			mBirth = DateTimeUtils.getDate()
		}
		mBirth = mBirth!! + " 00:00"
		val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
		val now = sdf.format(Date())
		dpl_date_picker.setStartAndEndTime("1900-01-01 00:00", now) // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
		dpl_date_picker.showSpecificTime(false) // 不显示时和分
		dpl_date_picker.setIsLoop(false) // 不允许循环滚动
		dpl_date_picker.setNow(mBirth)
	}

	override fun setListener() {

		tv_toolbar_right.setOnClickListener {
			val date = dpl_date_picker.selectTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
			changeBirthday(date)
		}
	}

	private fun changeBirthday(birth: String) {
		mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, birth, null)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main_loading(this))
				.subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
					override fun onNext(bean: UserInfoBean) {
						postSuccess(bean)
					}
				})
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
