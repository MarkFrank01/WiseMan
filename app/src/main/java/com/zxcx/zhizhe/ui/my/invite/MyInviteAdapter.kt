package com.zxcx.zhizhe.ui.my.invite

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.getColorForKotlin

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class MyInviteAdapter(data:List<InviteBean>):BaseQuickAdapter<InviteBean,BaseViewHolder>(R.layout.item_my_invite,data) {

    override fun convert(helper: BaseViewHolder, item: InviteBean) {
        val imageView = helper.getView<RoundedImageView>(R.id.invite_img)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.avatar)
        ImageLoader.load(mContext,imageUrl,R.drawable.default_header,imageView)

        helper.setText(R.id.invite_name,item.name)
        helper.setText(R.id.invite_time,ZhiZheUtils.timeChange(item.invitedTime))

        if (item.hasReceiveReward){
            helper.setText(R.id.invite_get,"已奖励")
            helper.setTextColor(R.id.invite_get,mContext.getColorForKotlin(R.color.white))
            helper.getView<CheckBox>(R.id.invite_get).setBackgroundResource(R.drawable.bg_backbutton2_grey)
        }else{
            helper.setText(R.id.invite_get,"领取奖励")
            helper.setTextColor(R.id.invite_get,mContext.getColorForKotlin(R.color.white))
            helper.getView<CheckBox>(R.id.invite_get).setBackgroundResource(R.drawable.bg_backbutton2)
        }
    }
}