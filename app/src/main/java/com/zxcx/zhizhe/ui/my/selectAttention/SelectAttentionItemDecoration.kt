package com.zxcx.zhizhe.ui.my.selectAttention

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * Created by anm on 2017/6/20.
 */

class SelectAttentionItemDecoration : RecyclerView.ItemDecoration() {

    private val defultSpace = ScreenUtils.dip2px(12f)
    private val bottomSpace = ScreenUtils.dip2px(15f)
    internal var space = ScreenUtils.dip2px(12f) / 2

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.left = ScreenUtils.dip2px(100f)
            1 -> outRect.left = ScreenUtils.dip2px(70f)
            2 -> {
            }
            3 -> outRect.left = ScreenUtils.dip2px(90f)
            4 -> outRect.left = ScreenUtils.dip2px(120f)
        }
    }
}
