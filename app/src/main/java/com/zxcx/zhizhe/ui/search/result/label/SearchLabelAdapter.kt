package com.zxcx.zhizhe.ui.search.result.label

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.TextViewUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SearchLabelAdapter(data:List<SearchLabelBean>):BaseQuickAdapter<SearchLabelBean,BaseViewHolder>(R.layout.item_search_label,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: SearchLabelBean) {
        val title = helper.getView<TextView>(R.id.tv_label_title)
        TextViewUtils.setTextViewColorBlue(title,mKeyword,"# "+item.title)
//        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_label_image)
//        val imageUrl = ZhiZheUtils.getHDImageUrl(item.titleImage)
//        ImageLoader.load(mContext,imageUrl,R.drawable.default_header,imageView)
        helper.addOnClickListener(R.id.re_to_label)
    }

}