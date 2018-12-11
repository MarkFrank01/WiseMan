package com.zxcx.zhizhe.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvp.entity.TemplateItem

/**
 * @author : MarkFrank01
 * @Created on 2018/12/7
 * @Description :
 */
class TemplateCardAdapter(data: List<TemplateItem>) : BaseQuickAdapter<TemplateItem, BaseViewHolder>(R.layout.item_template_card, data) {


    override fun convert(helper: BaseViewHolder, item: TemplateItem) {

        helper.addOnClickListener(R.id.content_view)
                .addOnClickListener(R.id.temp_img)
                .addOnClickListener(R.id.es)


    }
}