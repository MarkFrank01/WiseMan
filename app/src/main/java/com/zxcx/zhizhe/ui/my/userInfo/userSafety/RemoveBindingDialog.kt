package com.zxcx.zhizhe.ui.my.userInfo.userSafety

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.RemoveBindingEvent
import com.zxcx.zhizhe.mvpBase.CommonDialog
import com.zxcx.zhizhe.utils.TextViewUtils
import kotlinx.android.synthetic.main.dialog_single.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by anm on 2017/7/6.
 */

class RemoveBindingDialog : CommonDialog() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = inflater.inflate(R.layout.dialog_single, container)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val channel = arguments?.getString("channel")
		TextViewUtils.setTextViewColorBlue(tv_dialog_title, channel,
				getString(R.string.channel_unbind_title, channel))
	}

	override fun setListener() {
		super.setListener()
		tv_dialog_cancel.setOnClickListener {
			this.dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			EventBus.getDefault().post(RemoveBindingEvent())
			this.dismiss()
		}
	}
}
