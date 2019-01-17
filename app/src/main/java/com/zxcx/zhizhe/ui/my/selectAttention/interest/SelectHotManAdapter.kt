package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectHotManAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(AttentionManBean.TYPE1, R.layout.item_select_card_new_item_man)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when(helper.itemViewType){
            AttentionManBean.TYPE1 -> {
                val bean = item as AttentionManBean
                val imageView_head = helper.getView<RoundedImageView>(R.id.iv_man_head)
                ImageLoader.load(mContext,bean.avatar,R.drawable.default_header,imageView_head)

                helper.setText(R.id.iv_man_name,bean.name)
                helper.setText(R.id.iv_man_circle,bean.latestcircleTitle)
            }
        }
    }
}