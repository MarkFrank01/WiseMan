package com.zxcx.zhizhe.ui.circle.circlesearch.inside.qa

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.TextViewUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideQaAdapter (data: List<CircleDetailBean>) : BaseMultiItemQuickAdapter<CircleDetailBean, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.item_circle_search_huati)
    }

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: CircleDetailBean) {
        val itemBean = item

        val imageView = helper.getView<RoundedImageView>(R.id.circle_detail_img)
        if(itemBean.qaImageEntityList.isNotEmpty()){
            ImageLoader.load(mContext, itemBean.qaImageEntityList[0], R.drawable.default_card, imageView)
        }else{
            ImageLoader.load(mContext,R.drawable.default_card,imageView)
        }

        val UserImageView = helper.getView<RoundedImageView>(R.id.circle_detail_pushuser)
        ImageLoader.load(mContext, itemBean.usersVO.avater, R.drawable.default_card, UserImageView)


//        helper.setText(R.id.circle_detail_text, itemBean.title)
        val textView = helper.getView<TextView>(R.id.circle_detail_text)
        TextViewUtils.setTextViewColorBlue(textView,mKeyword,itemBean.title)

        helper.setText(R.id.circle_detail_content, itemBean.description)
        helper.setText(R.id.circle_detail_time,itemBean.modifiedTime)
        helper.setText(R.id.tv_item_card_read1,""+itemBean.likeCount)
        helper.setText(R.id.tv_item_card_comment1,""+itemBean.commentCount)
        helper.setText(R.id.circle_detail_username,itemBean.usersVO.name)

        helper.addOnClickListener(R.id.circle_detail_text)
        helper.addOnClickListener(R.id.circle_detail_img)

        helper.addOnClickListener(R.id.circle_detail_more)
    }

}