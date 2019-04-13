package com.zxcx.zhizhe.ui.circle.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/22
 * @Description :
 */
@Deprecated("弃用")
class CircleListItemAdapter(data:List<MultiItemEntity>):BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>(data){

    init {
        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_circle_list_iten_item)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when(helper.itemViewType){
            ClassifyBean.TYPE_CLASSIFY -> {

                val bean = item as ClassifyBean
                val para = helper.itemView.layoutParams
                val screenWidth = ScreenUtils.getDisplayWidth()
                helper.addOnClickListener(R.id.ll_item_circle_list_iten_item)

                helper.setText(R.id.tv_circle_list_iten_item,bean.title)

                para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 5

                helper.itemView.layoutParams = para
            }
        }
    }

}