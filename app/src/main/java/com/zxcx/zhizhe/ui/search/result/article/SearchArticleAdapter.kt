package com.zxcx.zhizhe.ui.search.result.article

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.TextViewUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class SearchArticleAdapter(data : List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_search_article,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: CardBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_search_card_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        val tvCardTitle = helper.getView<TextView>(R.id.tv_item_search_card_title)
        TextViewUtils.setTextViewColorBlue(tvCardTitle,mKeyword,item.name)
        val tvCardContent = helper.getView<TextView>(R.id.tv_item_search_card_content)
        TextViewUtils.setTextViewColorBlue(tvCardContent,mKeyword,item.searchContent)
    }

}