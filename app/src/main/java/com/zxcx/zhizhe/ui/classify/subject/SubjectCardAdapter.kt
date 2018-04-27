package com.zxcx.zhizhe.ui.classify.subject

import android.widget.ImageView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/6/26.
 */

class SubjectCardAdapter(data: List<CardBean>?) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_card, data) {

    init {
        setHeaderAndEmpty(true)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_card_bag, item.subjectName)
        helper.setText(R.id.tv_item_card_reade_num, item.readNum.toString() + "")
        helper.setText(R.id.tv_item_card_collect_num, item.collectNum.toString() + "")
        when (item.cardType) {
            1 -> helper.setText(R.id.tv_item_card_type, "卡片")
            2 -> helper.setText(R.id.tv_item_card_type, "长文")
        }
    }
}
