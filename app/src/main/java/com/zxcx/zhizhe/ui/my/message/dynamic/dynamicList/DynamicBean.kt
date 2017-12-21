package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by anm on 2017/12/20.
 */

const val dynamic_date = 1
const val dynamic_content = 2

data class DynamicBean( var date: String,var list: List<DynamicMessageListBean>): MultiItemEntity{
    override fun getItemType(): Int {
        return dynamic_date
    }
}