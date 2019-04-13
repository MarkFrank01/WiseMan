package com.zxcx.zhizhe.mvp.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author : MarkFrank01
 * @Created on 2018/12/8
 * @Description :
 */
class ClickEntity(private var Type:Int):MultiItemEntity {
    override fun getItemType(): Int {
        return Type
    }

    companion object {
        val CLICK_ITEM_VIEW = 1
        val CLICK_ITEM_CHILD_VIEW = 2
        val LONG_CLICK_ITEM_VIEW = 3
        val LONG_CLICK_ITEM_CHILD_VIEW = 4
        val NEST_CLICK_ITEM_CHILD_VIEW = 5
    }
}