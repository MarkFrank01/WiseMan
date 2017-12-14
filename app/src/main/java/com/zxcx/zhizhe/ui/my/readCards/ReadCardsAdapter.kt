package com.zxcx.zhizhe.ui.my.readCards

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.TextViewUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class ReadCardsAdapter(data : List<ReadCardsBean>) : BaseQuickAdapter<ReadCardsBean, BaseViewHolder>(R.layout.item_search_result_card,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: ReadCardsBean) {
        val title = helper.getView<TextView>(R.id.tv_item_search_result_card_name)
        TextViewUtils.setTextViewColorAndBold(title, mKeyword, item.name)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_search_result_card)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)
        helper.setText(R.id.tv_item_search_result_card_info, mContext.getString(R.string.tv_card_info, DateTimeUtils.getDateString(item.date), item.author))
        helper.setVisible(R.id.view_line,helper.adapterPosition != itemCount-1)
    }

}