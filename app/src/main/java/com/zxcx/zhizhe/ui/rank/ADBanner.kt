package com.zxcx.zhizhe.ui.rank

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.youth.banner.Banner
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * 广告轮播控件，固定宽高2:1
 */

class ADBanner(context: Context, attrs: AttributeSet) : Banner(context, attrs) {

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//		val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) / 2
		val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) / 2

        LogCat.e("widthMeasureSpec${ScreenUtils.getDisplayWidth()},jisuan$heightSize")

        val heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY)
		super.onMeasure(widthMeasureSpec, heightSpec)
	}

}