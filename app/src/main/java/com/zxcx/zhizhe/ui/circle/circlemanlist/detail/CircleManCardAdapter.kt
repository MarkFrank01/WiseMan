package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManCardAdapter(data:List<CardBean>):BaseMultiItemQuickAdapter<CardBean,BaseViewHolder>(data) {

    init {
        addItemType(CardBean.Article, R.layout.item_circle_man_card)
        addItemType(CardBean.Article_LONG, R.layout.item_circle_man_card)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {
        if (item.showTitle!=""&&item.showTitle.isNotEmpty()){
            helper.getView<TextView>(R.id.tv_daily_title).visibility = View.VISIBLE
            helper.setText(R.id.tv_daily_title,item.showTitle)
        }else{
            helper.getView<TextView>(R.id.tv_daily_title).visibility = View.GONE
        }

        if (item.showNumTitle!=""&&item.showNumTitle.isNotEmpty()){
            helper.getView<TextView>(R.id.tv_daily_title_num).visibility = View.VISIBLE
            helper.setText(R.id.tv_daily_title_num,item.showNumTitle)
        }else{
            helper.getView<TextView>(R.id.tv_daily_title_num).visibility = View.GONE

        }

        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_category, item.categoryName)
        if (item.labelName!=""&&item.labelName.isNotEmpty()) {
            helper.setText(R.id.tv_item_card_label, item.getLabelName())
        }else{
            helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
        }

        helper.setText(R.id.tv_item_card_read, item.readNum.toString())
        helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

        imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                R.string.card_title_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                R.string.card_category_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                R.string.card_label_transition_name)

        helper.setText(R.id.tv_item_card_time,item.distanceTime)

    }

}