package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.dynamic_content
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/5
 * @Description :
 */
class CircleNewAdapter(data:List<MultiItemEntity>):
        BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>(data){

    init {
        addItemType(dynamic_content, R.layout.item_more_circle_list_2)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when(helper.itemViewType){
            dynamic_content->{
                val item1 = item as CircleBean
                val imageUrl = ZhiZheUtils.getHDImageUrl(item1.titleImage)
                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView)

                helper.setText(R.id.tv_circle_title,item1.title)
                        .setText(R.id.more_circle_type,item1.classifyVO?.title)
                        .setText(R.id.more_circle_join_num,"加入"+item1.joinUserCount)
                        .setText(R.id.more_circle_huati_num,"话题"+item1.qaCount)

            }
        }
    }

}