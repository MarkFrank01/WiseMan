package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.getColorForKotlin


/**
 * Created by anm on 2017/5/23.
 * 选择标签页面，类别Adapter
 */

//class SelectClassifyOtherAdapter1(data: List<ClassifyBean>) : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.item_select_card_bag_circle, data) {
class SelectClassifyOtherAdapter1(data: List<ClassifyBean>) : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.item_select_new_bag, data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyBean) {

//            helper.addOnClickListener(R.id.fl_item_select_card_bag)
//            val para = helper.itemView.layoutParams
//            val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
//            para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 5
//            helper.itemView.layoutParams = para
//
//            helper.setText(R.id.cb_item_select_card_bag, item.title)
//            helper.setChecked(R.id.cb_item_select_card_bag, item.isChecked)

        helper.addOnClickListener(R.id.fl_item_select_card_bag)
        val para = helper.itemView.layoutParams
        val screenWidth = ScreenUtils.getDisplayWidth()
        para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 4
        helper.itemView.layoutParams = para

        helper.setText(R.id.cb_item_select_card_bag,item.title)
        helper.setChecked(R.id.cb_item_select_card_bag,item.isChecked)
        var checkBox = helper.getView<CheckBox>(R.id.cb_item_select_card_bag)

        if (item.isChecked){
            checkBox.setBackgroundResource(R.drawable.select_label)
            helper.setTextColor(R.id.cb_item_select_card_bag,mContext.getColorForKotlin(R.color.white))
        }else{
            checkBox.setBackgroundResource(R.drawable.select_unlabel)
            helper.setTextColor(R.id.cb_item_select_card_bag,mContext.getColorForKotlin(R.color.color_checkbox_text_select_attention))
        }
    }
}
