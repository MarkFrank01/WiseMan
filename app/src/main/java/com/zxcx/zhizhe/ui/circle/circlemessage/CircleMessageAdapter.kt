package com.zxcx.zhizhe.ui.circle.circlemessage

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
//class CircleMessageAdapter(data:List<MyCircleTabBean>):BaseQuickAdapter<MyCircleTabBean,BaseViewHolder>(R.layout.item_circle_message,data) {
class CircleMessageAdapter(data:List<MyCircleTabBean>):BaseMultiItemQuickAdapter<MyCircleTabBean,BaseViewHolder>(data) {

    init {
        addItemType(301,R.layout.item_circle_message_img)
        addItemType(302, R.layout.item_circle_message)
    }

    override fun convert(helper: BaseViewHolder, item: MyCircleTabBean) {
        when(item.itemType){
            MESSAGE_TYPE_CIRCLE_DYNAMIC_FIRST_LEVEL_COMMENT ->{
                val imageView = helper.getView<RoundedImageView>(R.id.message_round_img)
                ImageLoader.load(mContext,item.relatedUserAvatar,R.drawable.default_card,imageView)

                helper.setText(R.id.message_man_name,item.relatedUserName)
                helper.setText(R.id.message_man_time,item.distanceTime)
                helper.setText(R.id.message_comment,item.content)

                val relateImg = helper.getView<ImageView>(R.id.message_qa_img)
                ImageLoader.load(mContext,item.relatedQaTitleImage,R.drawable.default_card,relateImg)

                helper.setText(R.id.message_man_name_2,item.relatedQaTitle)

                val circleImg = helper.getView<ImageView>(R.id.message_circle_img)
                ImageLoader.load(mContext,item.relatedCircleTitleImage,R.drawable.default_card,circleImg)

                helper.setText(R.id.message_circle_title,item.relatedCircleTitle)

                helper.addOnClickListener(R.id.message_ll_1)
                        .addOnClickListener(R.id.con_ll)
            }

            MESSAGE_TYPE_CIRCLE_DYNAMIC_SECOND_LEVEL_COMMENT ->{
                val imageView = helper.getView<RoundedImageView>(R.id.message_round_img)
                ImageLoader.load(mContext,item.relatedUserAvatar,R.drawable.default_card,imageView)

                helper.setText(R.id.message_man_name,item.relatedUserName)
                helper.setText(R.id.message_man_time,item.distanceTime)
                helper.setText(R.id.message_comment,item.content)

                helper.setText(R.id.message_man_name_2,"我 ："+item.relatedCommentTitle)

                val circleImg = helper.getView<ImageView>(R.id.message_circle_img)
                ImageLoader.load(mContext,item.relatedCircleTitleImage,R.drawable.default_card,circleImg)

                helper.setText(R.id.message_circle_title,item.relatedCircleTitle)
            }
        }
    }
}