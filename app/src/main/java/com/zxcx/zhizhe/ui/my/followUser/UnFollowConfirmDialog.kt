package com.zxcx.zhizhe.ui.my.followUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.CommonDialog
import kotlinx.android.synthetic.main.dialog_single.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by anm on 2017/7/21.
 * 取消关注弹窗
 */

class UnFollowConfirmDialog : CommonDialog() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = inflater.inflate(R.layout.dialog_single, container)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tv_dialog_title.text = "是否取消对该用户的关注"
	}

	override fun setListener() {
		super.setListener()
		tv_dialog_cancel.setOnClickListener {
			this.dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			EventBus.getDefault().post(arguments?.getInt("userId")?.let { it1 -> UnFollowConfirmEvent(it1) })
			this.dismiss()
		}
	}
}
