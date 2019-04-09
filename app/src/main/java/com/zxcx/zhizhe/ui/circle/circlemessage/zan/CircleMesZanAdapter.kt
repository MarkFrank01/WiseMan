package com.zxcx.zhizhe.ui.circle.circlemessage.zan

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlemessage.MESSAGE_TYPE_CIRCLE_DYNAMIC_LIKE_CIRCLE
import com.zxcx.zhizhe.ui.circle.circlemessage.MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_ANSWER
import com.zxcx.zhizhe.ui.circle.circlemessage.MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_QUESTION
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesZanAdapter(data: List<MyCircleTabBean>) : BaseMultiItemQuickAdapter<MyCircleTabBean, BaseViewHolder>(data) {

    init {
        addItemType(303, R.layout.item_circle_message_zan_circle)
        addItemType(304, R.layout.item_circle_message_img_zan)
        addItemType(305, R.layout.item_circle_message_zan)
    }

    override fun convert(helper: BaseViewHolder, item: MyCircleTabBean) {
        when (item.itemType) {
            MESSAGE_TYPE_CIRCLE_DYNAMIC_LIKE_CIRCLE -> {
                val imageView = helper.getView<RoundedImageView>(R.id.message_round_img)
                ImageLoader.load(mContext, item.relatedUserAvatar, R.drawable.default_card, imageView)

                helper.setText(R.id.message_man_name, item.relatedUserName)
                helper.setText(R.id.message_man_time, item.distanceTime)
                helper.setText(R.id.message_comment, item.content)

                val relateImg = helper.getView<RoundedImageView>(R.id.message_qa_img)
                ImageLoader.load(mContext, item.relatedCircleTitleImage, R.drawable.default_card, relateImg)

                helper.setText(R.id.message_man_name_2, item.relatedCircleTitle)

                helper.addOnClickListener(R.id.message_ll_2)
            }

            MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_QUESTION -> {
                val imageView = helper.getView<RoundedImageView>(R.id.message_round_img)
                ImageLoader.load(mContext, item.relatedUserAvatar, R.drawable.default_card, imageView)

                helper.setText(R.id.message_man_name, item.relatedUserName)
                helper.setText(R.id.message_man_time, item.distanceTime)
                helper.setText(R.id.message_comment, item.content)

                val relateImg = helper.getView<ImageView>(R.id.message_qa_img)
                ImageLoader.load(mContext, item.relatedQaTitleImage, R.drawable.default_card, relateImg)

                helper.setText(R.id.message_man_name_2, item.relatedQaTitle)

                val circleImg = helper.getView<ImageView>(R.id.message_circle_img)
                ImageLoader.load(mContext, item.relatedCircleTitleImage, R.drawable.default_card, circleImg)

                helper.setText(R.id.message_circle_title, item.relatedCircleTitle)

                helper.addOnClickListener(R.id.message_ll_2)
            }

            MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_ANSWER -> {
                val imageView = helper.getView<RoundedImageView>(R.id.message_round_img)
                ImageLoader.load(mContext,item.relatedUserAvatar, R.drawable.default_card,imageView)

                helper.setText(R.id.message_man_name,item.relatedUserName)
                helper.setText(R.id.message_man_time,item.distanceTime)
                helper.setText(R.id.message_comment,item.content)

                helper.setText(R.id.message_man_name_2,"我 ："+item.relatedCommentTitle)

                val circleImg = helper.getView<ImageView>(R.id.message_circle_img)
                ImageLoader.load(mContext,item.relatedCircleTitleImage, R.drawable.default_card,circleImg)

                helper.setText(R.id.message_circle_title,item.relatedCircleTitle)

                helper.addOnClickListener(R.id.message_ll_2)
            }
        }
    }
}