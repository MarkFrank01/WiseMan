package com.zxcx.zhizhe.ui.circle.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleSixPicBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.widget.CardRoundedImageView


/**
 * @author : MarkFrank01
 * @Created on 2019/2/26
 * @Description :
 */
class CircleQuestionAdapter(data:List<CircleSixPicBean>):BaseMultiItemQuickAdapter<CircleSixPicBean,BaseViewHolder>(data){

    init {
        addItemType(1, R.layout.item_circle_question)
    }

    override fun convert(helper: BaseViewHolder, item: CircleSixPicBean) {
        val imageView = helper.getView<CardRoundedImageView>(R.id.circle_question_item_img)
        ImageLoader.load(mContext,item.image,R.drawable.default_card,imageView)
    }
}