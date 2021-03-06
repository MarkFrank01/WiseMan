package com.zxcx.zhizhe.ui.otherUser

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class OtherUserCardsAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_other_user_creation, data) {

	override fun convert(helper: BaseViewHolder, item: CardBean) {
		val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

		helper.setText(R.id.tv_item_card_title, item.name)
		helper.setText(R.id.tv_item_card_category, item.categoryName)
		helper.setText(R.id.tv_item_card_label, item.getLabelName())
		helper.setText(R.id.tv_item_card_read, item.readNum.toString())
		helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

        helper.setText(R.id.tv_item_card_time,item.distanceTime)
	}

}