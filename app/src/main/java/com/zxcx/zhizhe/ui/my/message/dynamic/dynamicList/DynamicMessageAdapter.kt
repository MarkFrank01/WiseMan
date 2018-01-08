package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.message.system.message_collect
import com.zxcx.zhizhe.ui.my.message.system.message_follow
import com.zxcx.zhizhe.ui.my.message.system.message_like
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.TextViewUtils

/**
 * Created by anm on 2017/5/23.
 */

class DynamicMessageAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {


    init {
        addItemType(dynamic_date, R.layout.item_dynamic_message_date)
        addItemType(dynamic_content, R.layout.item_dynamic_message)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            dynamic_date -> {
                val bean = item as DynamicBean
                helper.setText(R.id.tv_item_dynamic_message_date,bean.date)
            }
            dynamic_content -> {
                val bean = item as DynamicMessageListBean
                val imageView = helper.getView<RoundedImageView>(R.id.iv_item_dynamic_message)
                ImageLoader.load(mContext,bean.relatedUserAvatar,R.drawable.default_header,imageView)
                helper.setText(R.id.tv_item_dynamic_message_name,bean.relatedUserName)
                helper.setText(R.id.tv_item_dynamic_message_time,bean.newTime)

                if (helper.adapterPosition+1 >= data.size || getItem(helper.adapterPosition+1) is DynamicBean){
                    helper.setGone(R.id.view_line,false)
                }else{
                    helper.setGone(R.id.view_line,true)
                }

                val tvContent = helper.getView<TextView>(R.id.tv_item_dynamic_message_content)
                when(bean.messageType){
                    message_follow -> {
                        tvContent.text = bean.content
                    }
                    message_like -> {
                        TextViewUtils.setTextViewColorAndBold(tvContent,"《"+bean.relatedCardName+"》",bean.content)
                    }
                    message_collect -> {
                        TextViewUtils.setTextViewColorAndBold(tvContent,"《"+bean.relatedCardName+"》",bean.content)
                    }
                }
            }
        }
    }
}
