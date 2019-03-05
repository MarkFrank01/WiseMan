package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.ImageLoader


/**
 * Created by anm on 2017/5/23.
 * 选择标签页面，类别Adapter
 */

class SelectClassifyAdapter1(data: List<ClassifyBean>) : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.item_select_card_bag_circle, data) {



    override fun convert(helper: BaseViewHolder, item: ClassifyBean) {

//            helper.addOnClickListener(R.id.fl_item_select_card_bag)
//            val para = helper.itemView.layoutParams
//            val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
////		para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 4
//            para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 4
//            helper.itemView.layoutParams = para
//
//            helper.setText(R.id.cb_item_select_card_bag, item.title)
//            helper.setChecked(R.id.cb_item_select_card_bag, item.isChecked)

        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        ImageLoader.load(mContext,item.cover,R.drawable.default_card,imageView)

        helper.setText(R.id.tv_item_card_icon,item.title)

        helper.addOnClickListener(R.id.iv_item_card_icon)
        helper.addOnClickListener(R.id.tv_item_card_icon)

    }
}
