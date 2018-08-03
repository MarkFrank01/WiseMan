package com.zxcx.zhizhe.ui.my

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.window_sort.view.*


class SortWindow(val context: Context) : PopupWindow(context) {

	interface SortListener {
		fun defaultSort()
		fun earliestSort()
	}

	var mListener: SortListener? = null
	var sortType = 0
		set(value) {
			when (value) {
				0 -> {
					contentView.tv_sort_default.setTextColor(context.getColorForKotlin(R.color.button_blue))
					contentView.iv_sort_default.setImageResource(R.drawable.tv_sort_default_checked)
					contentView.tv_sort_earliest.setTextColor(context.getColorForKotlin(R.color.text_color_1))
					contentView.iv_sort_earliest.setImageResource(R.drawable.tv_sort_earliest)
				}
				1 -> {
					contentView.tv_sort_earliest.setTextColor(context.getColorForKotlin(R.color.button_blue))
					contentView.iv_sort_default.setImageResource(R.drawable.tv_sort_default)
					contentView.tv_sort_default.setTextColor(context.getColorForKotlin(R.color.text_color_1))
					contentView.iv_sort_earliest.setImageResource(R.drawable.tv_sort_earliest_checked)
				}
			}
		}

	init {
		val view = View.inflate(context, R.layout.window_sort, null)
		contentView = view
		width = ViewGroup.LayoutParams.WRAP_CONTENT
		height = ViewGroup.LayoutParams.WRAP_CONTENT
		setBackgroundDrawable(ColorDrawable(context.getColorForKotlin(R.color.translate)))
		isOutsideTouchable = true
		view.tv_sort_default.setOnClickListener {
			mListener?.defaultSort()
			dismiss()
		}
		view.tv_sort_earliest.setOnClickListener {
			mListener?.earliestSort()
			dismiss()
		}
	}

}