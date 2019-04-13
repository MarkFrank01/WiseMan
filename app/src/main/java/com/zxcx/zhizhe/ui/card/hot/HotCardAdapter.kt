package com.zxcx.zhizhe.ui.card.hot

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.CardRoundedImageView

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
//class HotCardAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_card, data) {
class HotCardAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(data) {

    /**
     * 特别注意此处的layout存在两种类型
     *  1 是普通的卡片
     *  2 是实用头条
     */
    init {
        addItemType(CardBean.Article, R.layout.item_card)
        addItemType(CardBean.Article_TOUTIAO, R.layout.item_toutiao)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {

        when (helper.itemViewType) {

            CardBean.Article -> {

                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
                ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

                helper.setText(R.id.tv_item_card_title, item.name)
                helper.setText(R.id.tv_item_card_category, item.categoryName)
                if (item.labelName!=""||item.labelName.isNotEmpty()) {
                    helper.setText(R.id.tv_item_card_label, item.getLabelName())
                }else{
                    helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
                }
                helper.setText(R.id.tv_item_card_read, item.readNum.toString())
                helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

                helper.setText(R.id.tv_item_card_time,item.distanceTime)

//                if (item.secondCollectionTitle != "" && item.secondCollectionTitle.isNotEmpty()) {
//                    helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
//                    helper.setText(R.id.tv_item_card_label2, item.getSecondLabelName())
//                }

                if (item.labelName!=""||item.labelName.isNotEmpty()||item.secondCollectionTitle.isEmpty()) {
                    helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.GONE
                }else{
                    helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
                    helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
                    helper.setText(R.id.tv_item_card_label2, item.getSecondLabelName())
                }

                imageView.transitionName = mContext.getString(R.string.card_img_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                        R.string.card_title_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                        R.string.card_category_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                        R.string.card_label_transition_name, helper.adapterPosition)
            }

            CardBean.Article_TOUTIAO -> {
                val roundImage = helper.getView<CardRoundedImageView>(R.id.iv_toutiao)
                roundImage.setImageResource(R.drawable.toutiao)

                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
                ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

                helper.setText(R.id.tv_item_card_title, item.name)
                helper.setText(R.id.tv_item_card_category, item.categoryName)
                if (item.labelName!=""||item.labelName.isNotEmpty()) {
                    helper.setText(R.id.tv_item_card_label, item.getLabelName())
                }else{
                    helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
                }
                helper.setText(R.id.tv_item_card_read, item.readNum.toString())
                helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

                helper.setText(R.id.tv_item_card_time,item.distanceTime)

//                if (item.secondCollectionTitle != "" && item.secondCollectionTitle.isNotEmpty()) {
//                    helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
//                    helper.setText(R.id.tv_item_card_label2, item.getSecondLabelName())
//                }

                imageView.transitionName = mContext.getString(R.string.card_img_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                        R.string.card_title_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                        R.string.card_category_transition_name, helper.adapterPosition)
                helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                        R.string.card_label_transition_name, helper.adapterPosition)

                helper.addOnClickListener(R.id.position_4)
                        .addOnClickListener(R.id.iv_toutiao)
            }
        }

    }
}
