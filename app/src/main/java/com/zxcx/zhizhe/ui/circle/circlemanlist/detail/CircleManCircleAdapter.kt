package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

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
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManCircleAdapter(data: List<MultiItemEntity>) :
        BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(dynamic_content, R.layout.item_circle_man_circle)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            dynamic_content -> {
                val item1 = item as CircleBean
                val imageUrl = ZhiZheUtils.getHDImageUrl(item1.titleImage)
                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

                helper.setText(R.id.more_price, "￥" + item.price)

                if (item.price =="0.00"){
                    helper.setText(R.id.more_price,"限免")
                    helper.setBackgroundRes(R.id.more_price,R.drawable.circle_price_red)
                }

                helper.setText(R.id.tv_circle_title, item1.title)
                        .setText(R.id.more_circle_type, item1.classifyVO?.title)
                        .setText(R.id.more_circle_join_num, "加入" + item1.joinUserCount)
                        .setText(R.id.more_circle_huati_num, "话题" + item1.qaCount)

                helper.addOnClickListener(R.id.to_content_circle)

                //圈子评分
                var num = item.overallRating
                val star_1 = helper.getView<ImageView>(R.id.star_1)
                val star_2 = helper.getView<ImageView>(R.id.star_2)
                val star_3 = helper.getView<ImageView>(R.id.star_3)
                val star_4 = helper.getView<ImageView>(R.id.star_4)
                val star_5 = helper.getView<ImageView>(R.id.star_5)

                when (num) {
                    0 -> {

                    }

                    1 -> {
                        ImageLoader.load(mContext, R.drawable.star_on, star_1)
                    }

                    2 -> {
                        ImageLoader.load(mContext, R.drawable.star_on, star_1)
                        ImageLoader.load(mContext, R.drawable.star_on, star_2)
                    }

                    3 -> {
                        ImageLoader.load(mContext, R.drawable.star_on, star_1)
                        ImageLoader.load(mContext, R.drawable.star_on, star_2)
                        ImageLoader.load(mContext, R.drawable.star_on, star_3)
                    }

                    4 -> {
                        ImageLoader.load(mContext, R.drawable.star_on, star_1)
                        ImageLoader.load(mContext, R.drawable.star_on, star_2)
                        ImageLoader.load(mContext, R.drawable.star_on, star_3)
                        ImageLoader.load(mContext, R.drawable.star_on, star_4)
                    }
                    5 -> {
                        ImageLoader.load(mContext, R.drawable.star_on, star_1)
                        ImageLoader.load(mContext, R.drawable.star_on, star_2)
                        ImageLoader.load(mContext, R.drawable.star_on, star_3)
                        ImageLoader.load(mContext, R.drawable.star_on, star_4)
                        ImageLoader.load(mContext, R.drawable.star_on, star_5)
                    }
                }


            }
        }
    }

}