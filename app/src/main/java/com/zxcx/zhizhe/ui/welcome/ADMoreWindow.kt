package com.zxcx.zhizhe.ui.welcome

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.window_ad_more.view.*

class ADMoreWindow(val context: Context) : PopupWindow(context) {

	interface ADMoreListener {
		fun refresh()
		fun share()
	}

	var mListener: ADMoreListener? = null

	init {
		val view = View.inflate(context, R.layout.window_ad_more, null)
		contentView = view
		width = ViewGroup.LayoutParams.WRAP_CONTENT
		height = ViewGroup.LayoutParams.WRAP_CONTENT
		setBackgroundDrawable(ColorDrawable(context.getColorForKotlin(R.color.translate)))
		isOutsideTouchable = true
		view.tv_ad_refresh.setOnClickListener {
			mListener?.refresh()
			dismiss()
		}
		view.tv_ad_share.setOnClickListener {
			mListener?.share()
			dismiss()
		}
	}

}