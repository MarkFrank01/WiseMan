package com.zxcx.zhizhe.ui.my.selectAttention.interest

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.expandViewTouchDelegate
import com.zxcx.zhizhe.utils.getColorForKotlin

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
//class SelectHotManAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
class SelectHotManAdapter(data: List<SearchUserBean>) : BaseQuickAdapter<SearchUserBean, BaseViewHolder>(R.layout.item_select_card_new_item_man,data) {

    override fun convert(helper: BaseViewHolder, item: SearchUserBean) {
        val imageView_head = helper.getView<RoundedImageView>(R.id.iv_man_head)
        ImageLoader.load(mContext,item.imageUrl,R.drawable.default_header,imageView_head)

        helper.setText(R.id.iv_man_name,item.name)
        if (item.latestcircleTitle!=""&& item.latestcircleTitle!!.isNotEmpty()) {
            helper.getView<TextView>(R.id.iv_man_circle).visibility = View.VISIBLE
            helper.setText(R.id.iv_man_circle, item.latestcircleTitle)
        }
        if (item.isFollow){
            helper.setTextColor(R.id.iv_man_name,mContext.getColorForKotlin(R.color.text_color_d2))
            helper.setTextColor(R.id.iv_man_circle,mContext.getColorForKotlin(R.color.text_color_d2))
        }else{
            helper.setTextColor(R.id.iv_man_name,mContext.getColorForKotlin(R.color.text_color_1))
            helper.setTextColor(R.id.iv_man_circle,mContext.getColorForKotlin(R.color.text_color_3))
        }

        helper.setChecked(R.id.iv_man_follow,item.isFollow)

        helper.getView<View>(R.id.iv_man_follow).expandViewTouchDelegate(ScreenUtils.dip2px(50f))
        helper.addOnClickListener(R.id.iv_man_follow)
    }

}