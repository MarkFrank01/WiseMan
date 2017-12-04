package com.zxcx.zhizhe.ui.search.result.user

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
class SearchUserAdapter(data : List<SearchUserBean>) : BaseQuickAdapter<SearchUserBean, BaseViewHolder>(R.layout.item_search_result_user,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: SearchUserBean) {
        val title = helper.getView<TextView>(R.id.tv_item_search_result_user_name)
        TextViewUtils.setTextViewColorAndBold(title, mKeyword, item.name)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_search_result_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        helper.setText(R.id.tv_item_search_result_user_card, (item.cardNum?:0).toString())
        helper.setText(R.id.tv_item_search_result_user_fans, (item.fansNum?:0).toString())
        helper.setText(R.id.tv_item_search_result_user_read, (item.readNum?:0).toString())
        helper.setVisible(R.id.view_line,helper.adapterPosition != itemCount-1)
        //todo 排名未做
    }

}