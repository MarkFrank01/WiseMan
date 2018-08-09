package com.zxcx.zhizhe.widget

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.airbnb.lottie.LottieDrawable
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseDialog
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.layout_uploading.*

/**
 * Created by anm on 2017/5/27.
 * 提交审核，保存笔记弹窗
 */

class UploadingDialog : BaseDialog() {

	var uploadingText: String = ""
	var successText: String = ""
	var failText: String = ""

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		isCancelable = false
		val view = inflater.inflate(R.layout.layout_uploading, container)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		uploadingText = arguments?.getString("uploadingText") ?: ""
		successText = arguments?.getString("successText") ?: ""
		failText = arguments?.getString("failText") ?: ""
		iv_loading.setAnimation("loading.json")
		iv_loading.repeatCount = LottieDrawable.INFINITE
		tv_uploading.text = uploadingText
	}

	fun setSuccess(isSuccess: Boolean) {
		iv_loading.repeatCount = 0
		if (isSuccess) {
			iv_loading.setAnimation("load_complete.json")
		} else {
			iv_loading.setAnimation("load_fail.json")
		}
		tv_uploading.text = if (isSuccess) successText else failText
		iv_loading.playAnimation()
		Handler().postDelayed({
			iv_loading.setAnimation("loading.json")
			dismiss()
		}, 1000)
	}

	override fun onStart() {
		super.onStart()

		val dialog = dialog
		if (dialog != null) {
			val window = dialog.window
			window!!.setBackgroundDrawableResource(R.color.translate)
			val lp = window.attributes
			lp.dimAmount = 0.0f
			lp.width = ScreenUtils.dip2px(140f)
			lp.height = ScreenUtils.dip2px(140f)
			window.attributes = lp
		}
	}

	override fun onDestroyView() {
		//		((AnimationDrawable) mIvLoading.getDrawable()).stop();
		super.onDestroyView()
	}
}
