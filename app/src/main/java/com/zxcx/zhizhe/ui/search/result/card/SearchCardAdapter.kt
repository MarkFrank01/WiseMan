package com.zxcx.zhizhe.ui.search.result.card

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
class SearchCardAdapter(data : List<SearchCardBean>) : BaseQuickAdapter<SearchCardBean, BaseViewHolder>(R.layout.item_search_result,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: SearchCardBean) {
        val title = helper.getView<TextView>(R.id.tv_item_search_result_title)
        val content = helper.getView<TextView>(R.id.tv_item_search_result_content)
        TextViewUtils.setTextViewColor(title, mKeyword, item.name)
        TextViewUtils.setTextViewColor(content, mKeyword, item.content)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_search_result_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)
        helper.setText(R.id.tv_item_search_result_card_bag, item.cardBagName)
        helper.setText(R.id.tv_item_search_result_collect, item.collectNum.toString() + "")
        helper.setText(R.id.tv_item_search_result_read, item.readNum.toString() + "")
    }

}