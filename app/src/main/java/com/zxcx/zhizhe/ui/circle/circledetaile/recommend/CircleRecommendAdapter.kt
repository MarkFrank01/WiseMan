package com.zxcx.zhizhe.ui.circle.circledetaile.recommend

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/3/18
 * @Description :
 */
class CircleRecommendAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(data) {

    init {
        addItemType(CardBean.Article, R.layout.item_recommend_arc)
        addItemType(CardBean.Article_LONG, R.layout.item_recommend_arc)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {
        when (helper.itemViewType) {
            CardBean.Article -> {
                val imageView = helper.getView<RoundedImageView>(R.id.recommend_img)
//                imageView.setBackgroundColor(mContext.getColorForKotlin(R.color.button_blue))
                ImageLoader.load(mContext,R.drawable.circle_card_jx,imageView)

                helper.setText(R.id.recommend_text,item.name)
                helper.addOnClickListener(R.id.recommend_img)
                        .addOnClickListener(R.id.recommend_text)
            }

            CardBean.Article_LONG ->{
                val imageView = helper.getView<RoundedImageView>(R.id.recommend_img)
//                imageView.setBackgroundColor(mContext.getColorForKotlin(R.color.red))
                ImageLoader.load(mContext,R.drawable.circle_arc_jx,imageView)

                helper.setText(R.id.recommend_text,item.name)
                helper.addOnClickListener(R.id.recommend_img)
                        .addOnClickListener(R.id.recommend_text)

            }
        }
    }


}