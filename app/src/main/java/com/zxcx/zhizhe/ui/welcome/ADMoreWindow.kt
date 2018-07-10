package com.zxcx.zhizhe.ui.welcome

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.window_sort.view.*

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
		view.tv_sort_default.setOnClickListener {
			mListener?.refresh()
			dismiss()
		}
		view.tv_sort_earliest.setOnClickListener {
			mListener?.share()
			dismiss()
		}
	}

}