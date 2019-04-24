package com.zxcx.zhizhe.ui.topchange

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
//class TopChange1Adapter(data:List<ClassifyBean>):BaseQuickAdapter<ClassifyBean,BaseViewHolder>(R.layout.item_top_change_1) {
class TopChange1Adapter(data:List<ClassifyBean>):BaseItemDraggableAdapter<ClassifyBean,BaseViewHolder>(R.layout.item_top_change_1,data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyBean) {
        helper.addOnClickListener(R.id.fl_item_select_card_bag)
        val para = helper.itemView.layoutParams
        val screenWidth = ScreenUtils.getDisplayWidth()
        para.width = ((screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((10 * 2).toFloat())) / 4)-6
//        para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((10 * 2).toFloat())) / 4
        LogCat.e("para.width"+para.width)
        helper.itemView.layoutParams = para

        helper.setText(R.id.cb_item_select_card_bag,item.title)
        if (item.isChecked){
            helper.getView<ImageView>(R.id.can_show_cancel).visibility = View.VISIBLE
            helper.getView<CheckBox>(R.id.cb_item_select_card_bag).setPadding(10,0,0,0)
        }else{
            helper.getView<ImageView>(R.id.can_show_cancel).visibility = View.GONE
            helper.getView<CheckBox>(R.id.cb_item_select_card_bag).setPadding(0,0,0,0)

        }


//        helper.setChecked(R.id.cb_item_select_card_bag,item.isFollow)
//        var checkBox = helper.getView<CheckBox>(R.id.cb_item_select_card_bag)
//
//        if (item.isFollow){
//            checkBox.setBackgroundResource(R.drawable.select_label)
//            helper.setTextColor(R.id.cb_item_select_card_bag,mContext.getColorForKotlin(R.color.white))
//        }else{
//            checkBox.setBackgroundResource(R.drawable.select_unlabel)
//            helper.setTextColor(R.id.cb_item_select_card_bag,mContext.getColorForKotlin(R.color.color_checkbox_text_select_attention))
//        }
    }
}