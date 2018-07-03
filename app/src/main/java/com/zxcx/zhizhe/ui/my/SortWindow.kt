package com.zxcx.zhizhe.ui.my

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.TextViewUtils
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
					TextViewUtils.setTextLeftDrawable(context, R.drawable.bg_checkbox_follow_checked, contentView.tv_sort_default)
					contentView.tv_sort_earliest.setTextColor(context.getColorForKotlin(R.color.text_color_1))
					TextViewUtils.setTextLeftDrawable(context, R.drawable.bg_checkbox_follow_checked, contentView.tv_sort_earliest)
				}
				1 -> {
					contentView.tv_sort_earliest.setTextColor(context.getColorForKotlin(R.color.button_blue))
					TextViewUtils.setTextLeftDrawable(context, R.drawable.bg_checkbox_follow_checked, contentView.tv_sort_earliest)
					contentView.tv_sort_default.setTextColor(context.getColorForKotlin(R.color.text_color_1))
					TextViewUtils.setTextLeftDrawable(context, R.drawable.bg_checkbox_follow_checked, contentView.tv_sort_default)
				}
			}
		}

	init {
		val view = View.inflate(context, R.layout.item_article, null)
		contentView = view
		width = ViewGroup.LayoutParams.WRAP_CONTENT
		height = ViewGroup.LayoutParams.WRAP_CONTENT
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