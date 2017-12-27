package com.zxcx.zhizhe.ui.search.result.card

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class FollowUserAdapter(data : List<FollowUserBean>) : BaseQuickAdapter<FollowUserBean, BaseViewHolder>(R.layout.item_follow_user,data){

    override fun convert(helper: BaseViewHolder, item: FollowUserBean) {
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_follow_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        helper.setText(R.id.tv_item_follow_user_name, item.name)
        helper.setText(R.id.tv_item_follow_user_card, (item.cardNum?:0).toString())
        helper.setText(R.id.tv_item_follow_user_fans, (item.fansNum?:0).toString())
        helper.setText(R.id.tv_item_follow_user_read, (item.readNum?:0).toString())
        helper.setVisible(R.id.view_line,helper.adapterPosition != itemCount-1)

        helper.setChecked(R.id.cb_item_follow_user,item.followType != 0)
        when(item.followType){
            0 -> helper.setText(R.id.cb_item_follow_user,"未关注")
            1 -> helper.setText(R.id.cb_item_follow_user,"已关注")
            2 -> helper.setText(R.id.cb_item_follow_user,"互相关注")
        }

        helper.addOnClickListener(R.id.cb_item_follow_user)
    }

}