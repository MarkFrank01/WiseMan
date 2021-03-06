package com.zxcx.zhizhe.ui.article.subject

import android.widget.ImageView
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/6/26.
 */

class SubjectArticleAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_article, data) {

	init {
		setHeaderAndEmpty(true)
	}

	override fun convert(helper: BaseViewHolder, item: CardBean) {
		val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

		helper.setText(R.id.tv_item_card_title, item.name)
		helper.setText(R.id.tv_item_card_category, item.categoryName)
		helper.setText(R.id.tv_item_card_label, item.getLabelName())
		helper.setText(R.id.tv_item_card_read, item.readNum.toString())
		helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

		imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
		helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
				R.string.card_title_transition_name)
		helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
				R.string.card_category_transition_name)
		helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
				R.string.card_label_transition_name)
	}
}
