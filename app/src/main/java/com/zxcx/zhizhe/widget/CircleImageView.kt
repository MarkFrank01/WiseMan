package com.zxcx.zhizhe.widget

import android.content.Context
import android.util.AttributeSet
import com.makeramen.roundedimageview.RoundedImageView


/**
 * Created by anm on 2018/3/20.
 */

class CircleImageView(context: Context, attrs: AttributeSet) : RoundedImageView(context, attrs) {

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val w = this.measuredWidth
		setMeasuredDimension(w, w)
	}
}
