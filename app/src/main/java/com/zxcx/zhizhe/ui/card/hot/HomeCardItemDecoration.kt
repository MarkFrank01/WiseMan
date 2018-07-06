package com.zxcx.zhizhe.ui.card.hot

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * Created by anm on 2017/6/20.
 */

class HomeCardItemDecoration : RecyclerView.ItemDecoration() {

	private val defultSpace = ScreenUtils.dip2px(20f)

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
	                            state: RecyclerView.State?) {
		val position = parent.getChildAdapterPosition(view)
		if (position == 0) {
			outRect.top = defultSpace
		}
	}
}
