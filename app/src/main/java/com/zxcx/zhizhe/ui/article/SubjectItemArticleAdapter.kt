package com.zxcx.zhizhe.ui.article

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

class SubjectItemArticleAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_subject_article, data) {

	override fun convert(helper: BaseViewHolder, item: CardBean) {
		val imageView = helper.getView<ImageView>(R.id.iv_item_subject_article)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

		helper.setText(R.id.tv_item_subject_article_title, item.name)
		helper.setText(R.id.tv_item_subject_article_read, item.readNum.toString())
		helper.setText(R.id.tv_item_subject_article_comment, item.commentNum.toString())

		imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
		helper.getView<TextView>(R.id.tv_item_subject_article_title).transitionName = mContext.getString(
				R.string.card_title_transition_name)
	}
}
