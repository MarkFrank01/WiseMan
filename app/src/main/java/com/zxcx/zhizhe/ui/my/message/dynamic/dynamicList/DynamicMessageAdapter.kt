package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * Created by anm on 2017/5/23.
 */

class DynamicMessageAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

	init {
		setHeaderAndEmpty(true)
		addItemType(dynamic_date, R.layout.item_dynamic_message_date)
		addItemType(dynamic_content, R.layout.item_dynamic_message)
	}

	override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
		when (helper.itemViewType) {
			dynamic_date -> {
				val bean = item as DynamicBean
				helper.setText(R.id.tv_item_dynamic_message_date, bean.date)
			}
			dynamic_content -> {
				val bean = item as DynamicMessageListBean
				val imageView = helper.getView<RoundedImageView>(R.id.iv_item_dynamic_message)
				ImageLoader.load(mContext, bean.relatedUserAvatar, R.drawable.default_header, imageView)
				helper.setText(R.id.tv_item_dynamic_message_name, bean.relatedUserName)
				helper.setText(R.id.tv_item_dynamic_message_date, bean.newTime)
				helper.setText(R.id.tv_item_dynamic_message_content, bean.content)
				helper.setText(R.id.tv_item_dynamic_message_card, bean.relatedCardName)
				helper.setGone(R.id.tv_item_dynamic_message_content, bean.content.isNotEmpty())
				helper.setGone(R.id.tv_item_dynamic_message_card, bean.relatedCardName.isNotEmpty())

				helper.addOnClickListener(R.id.iv_item_dynamic_message)
				helper.addOnClickListener(R.id.tv_item_dynamic_message_name)
				helper.addOnClickListener(R.id.tv_item_dynamic_message_content)
				helper.addOnClickListener(R.id.tv_item_dynamic_message_card)

                val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)
                if (bean.authenticationType!=0&&bean.authenticationType==1){
                    imageVIP.visibility = View.VISIBLE
                }
			}
		}
	}
}
