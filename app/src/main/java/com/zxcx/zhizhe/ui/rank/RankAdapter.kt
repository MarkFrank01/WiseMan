package com.zxcx.zhizhe.ui.rank

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.TextViewUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class RankAdapter(data: List<UserRankBean>) : BaseQuickAdapter<UserRankBean, BaseViewHolder>(R.layout.item_rank_user, data) {

	var mKeyword = ""

	init {
		setHeaderAndEmpty(true)
	}

	override fun convert(helper: BaseViewHolder, item: UserRankBean) {
		val title = helper.getView<TextView>(R.id.tv_item_rank_user_name)
		TextViewUtils.setTextViewColorBlue(title, mKeyword, item.name)
		val imageView = helper.getView<RoundedImageView>(R.id.iv_item_rank_user)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
		helper.setText(R.id.tv_item_rank_user_card, item.cardNum.toString())
		helper.setText(R.id.tv_item_rank_user_fans, item.fansNum.toString())
		helper.setText(R.id.tv_item_rank_user_like, item.likeNum.toString())
		helper.setText(R.id.tv_item_rank_user_collect, item.collectNum.toString())
		helper.setText(R.id.tv_item_rank_user_level, item.intelligence.toString())
		helper.setText(R.id.tv_item_rank_user_rank, item.rankIndex.toString())
	}

}