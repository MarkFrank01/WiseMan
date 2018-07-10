package com.zxcx.zhizhe.ui.loginAndRegister.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneConfirmEvent
import com.zxcx.zhizhe.mvpBase.CommonDialog
import com.zxcx.zhizhe.utils.TextViewUtils
import kotlinx.android.synthetic.main.dialog_single.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by anm on 2017/7/21.
 */

class PhoneConfirmDialog : CommonDialog() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		return inflater.inflate(R.layout.dialog_single, container)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val phone = arguments?.getString("phone")
		TextViewUtils.setTextViewColorBlue(tv_dialog_title, phone,
				getString(R.string.tv_dialog_phone_confirm_title, phone))
	}

	override fun setListener() {
		super.setListener()
		tv_dialog_cancel.setOnClickListener {
			this.dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			EventBus.getDefault().post(PhoneConfirmEvent())
			this.dismiss()
		}
	}
}
