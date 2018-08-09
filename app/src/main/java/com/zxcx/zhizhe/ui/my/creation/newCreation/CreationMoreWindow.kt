package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.window_creation_more.view.*

/**
 * 创作编辑器-右下角更多弹窗
 */

class CreationMoreWindow(val context: Context, val isCard: Boolean) : PopupWindow(context) {

	lateinit var mPreviewListener: () -> Unit
	lateinit var mSaveListener: () -> Unit
	lateinit var mTypeListener: () -> Unit
	lateinit var mDeleteListener: () -> Unit

	init {
		val view = View.inflate(context, R.layout.window_creation_more, null)
		contentView = view
		width = ViewGroup.LayoutParams.WRAP_CONTENT
		height = ViewGroup.LayoutParams.WRAP_CONTENT
		setBackgroundDrawable(ColorDrawable(context.getColorForKotlin(R.color.translate)))
		isOutsideTouchable = true

		view.tv_creation_type.text = if (isCard) "切换长文" else "切换卡片"

		view.tv_creation_preview.setOnClickListener {
			mPreviewListener.invoke()
			dismiss()
		}
		view.tv_creation_save.setOnClickListener {
			mSaveListener.invoke()
			dismiss()
		}
		view.tv_creation_type.setOnClickListener {
			mTypeListener.invoke()
			dismiss()
		}
		view.tv_creation_delete.setOnClickListener {
			mDeleteListener.invoke()
			dismiss()
		}
	}

}