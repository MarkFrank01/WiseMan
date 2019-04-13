package com.zxcx.zhizhe.mvp.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author : MarkFrank01
 * @Created on 2018/12/7
 * @Description :
 */
class TemplateItem : MultiItemEntity {

    //模板类型编号
    private var itemType: Int = 0

    //模板名称
     var mTempName: String? = null

    //模板样式
//    var mTempImage: String? = null

    //间距距离
    var spanSize: Int = 0

    constructor(itemType: Int, mTempName: String, spanSize: Int) {
        this.itemType = itemType
        this.mTempName = mTempName
        this.spanSize = spanSize
    }

    constructor(itemType: Int, spanSize: Int) {
        this.itemType = itemType
        this.spanSize = spanSize
    }


    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        val TEMP_0 = 0
        val TEMP_1 = 1
        val TEMP_2 = 2
        val TEMP_3 = 3
        val TEMP_4 = 4
        val TEMP_5 = 5
        val TEMP_6 = 6
        val TEMP_7 = 7
        val TEMP_8 = 8
        val TEMP_9 = 9
        val IMG_TEXT_SPAN_SIZE_MIN = 2
    }
}