package com.zxcx.zhizhe.ui.topchange

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
class TopChange2Adapter(data:List<ClassifyBean>):BaseQuickAdapter<ClassifyBean,BaseViewHolder>(R.layout.item_top_change_2) {

    override fun convert(helper: BaseViewHolder, item: ClassifyBean) {
        helper.addOnClickListener(R.id.fl_item_select_card_bag)
        val para = helper.itemView.layoutParams
        val screenWidth = ScreenUtils.getDisplayWidth()
        para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((10 * 2).toFloat())) / 4
        helper.itemView.layoutParams = para

        helper.setText(R.id.cb_item_select_card_bag,item.title)
    }
}