package com.zxcx.zhizhe.widget

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.zxcx.zhizhe.R

/**
 * Created by anm on 2017/12/15.
 */

object EmptyView {
    @JvmStatic
    fun getEmptyView(context: Context, str1: String, str2: String, @ColorRes str2Color: Int?, listener: View.OnClickListener?): View {
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null)
        emptyView.findViewById<TextView>(R.id.tv_no_data_1).text = str1
        emptyView.findViewById<TextView>(R.id.tv_no_data_2).text = str2
        str2Color?.let { emptyView.findViewById<TextView>(R.id.tv_no_data_2).setTextColor(ContextCompat.getColor(context, it)) }
        emptyView.setOnClickListener(listener)
        return emptyView
    }
}
