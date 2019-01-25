package com.zxcx.zhizhe.mvp.entity

/**
 * @author : MarkFrank01
 * @Created on 2018/12/7
 * @Description : 预设置加载的数据
 */
open class DataServer private constructor() {

    companion object {
        fun getTempItemData(): List<TemplateItem> {
            val list = ArrayList<TemplateItem>()

            list.add(TemplateItem(TemplateItem.TEMP_0, "Basic", TemplateItem.TEMP_1))
            list.add(TemplateItem(TemplateItem.TEMP_1, "First", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_2, "Second", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_3, "Third", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_4, "Four", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_5, "Five", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_6, "six", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_7, "seven", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_8, "eight", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))
            list.add(TemplateItem(TemplateItem.TEMP_9, "nine", TemplateItem.IMG_TEXT_SPAN_SIZE_MIN))

            return list
        }
    }
}