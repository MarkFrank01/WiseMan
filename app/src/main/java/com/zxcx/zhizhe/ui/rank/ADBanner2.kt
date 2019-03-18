package com.zxcx.zhizhe.ui.rank

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.youth.banner.Banner
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * 广告轮播控件，固定宽高2:1
 */

class ADBanner2(context: Context, attrs: AttributeSet) : Banner(context, attrs) {

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//		val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) / 2
        //16：6
//		val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) * 3f /8f

        //16:4
        val heightSize = (ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2) * 4f / 7f

//        LogCat.e("Base"+(ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 2))
//        LogCat.e("HeightSize is $heightSize")
//        LogCat.e("widthMeasureSpec${ScreenUtils.getDisplayWidth()},Utils${ScreenUtils.dip2px(20f)},jisuan$heightSize")
//        LogCat.e("bili:${heightSize.toFloat()/ScreenUtils.getDisplayWidth().toFloat()}")

        val heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize.toInt(), View.MeasureSpec.EXACTLY)
		super.onMeasure(widthMeasureSpec, heightSpec)
	}

}