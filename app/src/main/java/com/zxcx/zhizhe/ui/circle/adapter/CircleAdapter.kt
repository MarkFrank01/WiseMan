package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleHomeOnClickListener
import com.zxcx.zhizhe.ui.circle.circlehome.dynamic_content
import com.zxcx.zhizhe.ui.circle.circlehome.dynamic_date
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleAdapter(data: List<MultiItemEntity>, private val mListener: CircleHomeOnClickListener) :
        BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(dynamic_date, R.layout.item_more_circle_list_title)
        addItemType(dynamic_content, R.layout.item_more_circle_list)
    }


    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            dynamic_date -> {
//                val bean = item as CircleChooseBean
//                helper.setText(R.id.tv_item_dynamic_message_date,bean.date)
            }
            dynamic_content -> {
                val item1 = item as CircleBean
                val imageUrl = ZhiZheUtils.getHDImageUrl(item1.titleImage)
                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

                helper.setText(R.id.tv_circle_title, item1.title)
                helper.setText(R.id.tv_item_card_title, item1.sign)
                helper.setText(R.id.tv_item_circle_input, "" + item1.joinUserCount)
                helper.setText(R.id.tv_item_circle_comment, "" + item1.qaCount)

                helper.addOnClickListener(R.id.to_content_circle)
                if (item1.showTitle!=""&&item1.showTitle.isNotEmpty()){
                    helper.setText(R.id.tv_circle_classify_title,item1.showTitle)
                }
//                helper.setText(R.id.tv_circle_classify_title, item1.classifytitle)
            }
        }
    }

}