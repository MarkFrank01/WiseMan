package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SelectHotLabelAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(ClassifyCardBean.TYPE_CARD_BAG, R.layout.item_select_card_new)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            ClassifyCardBean.TYPE_CARD_BAG -> {
                helper.addOnClickListener(R.id.cb_item_select_hot_label)
                helper.addOnClickListener(R.id.fl_item_select_card_bag)

                val cardBean = item as ClassifyCardBean
                helper.setText(R.id.cb_item_select_hot_label, "# "+cardBean.name)
                helper.setChecked(R.id.cb_item_select_hot_label, cardBean.follow)

                val para = helper.itemView.layoutParams
                val screenWidth = ScreenUtils.getDisplayWidth()

                if (cardBean.name?.length!! > 5) {
                    para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 2
                } else {
                    para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 3
                }
                helper.itemView.layoutParams = para
            }
        }
    }

}