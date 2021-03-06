package com.zxcx.zhizhe.ui.circle.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/2/21
 * @Description :
 */
class CircleDetaileAdapter(data: List<CircleDetailBean>) : BaseMultiItemQuickAdapter<CircleDetailBean, BaseViewHolder>(data) {


    init {
        addItemType(1, R.layout.item_circle_detail_huati)
    }

    override fun convert(helper: BaseViewHolder, item: CircleDetailBean) {
        val itemBean = item as CircleDetailBean

        val imageView = helper.getView<RoundedImageView>(R.id.circle_detail_img)
        if(itemBean.qaImageEntityList.isNotEmpty()){
            ImageLoader.load(mContext, itemBean.qaImageEntityList[0], R.drawable.default_card, imageView)
        }else{
            imageView.visibility = View.GONE
        }

        val UserImageView = helper.getView<RoundedImageView>(R.id.circle_detail_pushuser)
        ImageLoader.load(mContext, itemBean.usersVO.avater, R.drawable.default_card, UserImageView)


        helper.setText(R.id.circle_detail_text, itemBean.title)
        helper.setText(R.id.circle_detail_content, itemBean.description)
        helper.setText(R.id.circle_detail_time,itemBean.distanceTime)
        helper.setText(R.id.tv_item_card_read1,""+itemBean.likeCount)
        helper.setText(R.id.tv_item_card_comment1,""+itemBean.commentCount)
        helper.setText(R.id.circle_detail_username,itemBean.usersVO.name)

        helper.addOnClickListener(R.id.circle_detail_text)
        helper.addOnClickListener(R.id.circle_detail_img)

        helper.addOnClickListener(R.id.circle_detail_more)
        helper.addOnClickListener(R.id.circle_detail_content)
    }


}