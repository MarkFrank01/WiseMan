package com.zxcx.zhizhe.ui.circle.circlehome

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by anm on 2017/12/20.
 */

const val dynamic_date = 1
const val dynamic_content = 2

data class CircleChooseBean(var date: String, var list: List<CircleBean>) : MultiItemEntity {
	override fun getItemType(): Int {
		return dynamic_date
	}
}