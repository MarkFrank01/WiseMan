package com.zxcx.zhizhe.ui.card.cardDetails

import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pixplicity.htmlcompat.HtmlCompat
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/6/26.
 */

class CardDetailsAdapter(data: List<CardDetailsBean>) : BaseQuickAdapter<CardDetailsBean, BaseViewHolder>(R.layout.item_card,data) {

    override fun convert(helper: BaseViewHolder, item: CardDetailsBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)

        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_category, item.cardCategoryName)
        helper.setText(R.id.tv_item_card_label, item.cardLabelName)
        val fromHtml = HtmlCompat.fromHtml(mContext, item.content, 0)
        val tvContent = helper.getView<TextView>(R.id.tv_item_card_details_content)
        tvContent.movementMethod = LinkMovementMethod.getInstance()
        tvContent.text = fromHtml
    }
}
