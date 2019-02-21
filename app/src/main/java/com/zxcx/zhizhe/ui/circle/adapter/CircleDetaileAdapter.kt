package com.zxcx.zhizhe.ui.circle.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R

/**
 * @author : MarkFrank01
 * @Created on 2019/2/21
 * @Description :
 */
class CircleDetaileAdapter(data:List<MultiItemEntity>):
        BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>(data){

    init {
        addItemType(1, R.layout.item_circle_detail_huati)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
    }

}