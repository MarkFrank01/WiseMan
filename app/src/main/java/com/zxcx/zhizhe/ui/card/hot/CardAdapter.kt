package com.zxcx.zhizhe.ui.card.hot

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/6/26.
 * 通用卡片Adapter
 */

class CardAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_card, data) {

	override fun convert(helper: BaseViewHolder, item: CardBean) {
		val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

		helper.setText(R.id.tv_item_card_title, item.name)
		helper.setText(R.id.tv_item_card_category, item.categoryName)
//		helper.setText(R.id.tv_item_card_label, item.getLabelName())
		helper.setText(R.id.tv_item_card_read, item.readNum.toString())
		helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

        if (item.labelName!=""||item.labelName.isNotEmpty()) {
            helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.VISIBLE
            helper.setText(R.id.tv_item_card_label, item.getLabelName())
        }else{
            helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
        }

        if (item.secondCollectionTitle!=""&&item.secondCollectionTitle.isNotEmpty()&&item.labelName==""){
            helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
            helper.setText(R.id.tv_item_card_label2,item.getSecondLabelName())
        }

		imageView.transitionName = mContext.getString(R.string.card_img_transition_name, helper.adapterPosition)
		helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
				R.string.card_title_transition_name, helper.adapterPosition)
		helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
				R.string.card_category_transition_name, helper.adapterPosition)
		helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
				R.string.card_label_transition_name, helper.adapterPosition)

        helper.setText(R.id.tv_item_card_time,item.distanceTime)
	}
}
