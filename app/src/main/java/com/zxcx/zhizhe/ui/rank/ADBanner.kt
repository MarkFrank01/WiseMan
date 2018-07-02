package com.zxcx.zhizhe.ui.rank

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.youth.banner.Banner
import com.zxcx.zhizhe.utils.ScreenUtils

class ADBanner(context: Context, attrs: AttributeSet) : Banner(context, attrs) {

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) * 9 / 16
		val heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY)
		super.onMeasure(widthMeasureSpec, heightSpec)
	}

}