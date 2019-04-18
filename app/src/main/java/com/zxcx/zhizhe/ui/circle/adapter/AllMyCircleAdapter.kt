package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.getColorForKotlin

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCircleAdapter(data:List<CircleBean>):
        BaseMultiItemQuickAdapter<CircleBean, BaseViewHolder>(data){

    init {
        addItemType(CircleBean.CIRCLE_HOME_1, R.layout.item_circle_classify2)
    }

    override fun convert(helper: BaseViewHolder, item: CircleBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_circle_classify_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.titleImage)
        ImageLoader.load(mContext,imageUrl, R.drawable.default_card,imageView)

        helper.setText(R.id.tv_item_circle_classify_title,item.title)
        helper.setText(R.id.tv_item_circle_classify_desc,item.sign)
        helper.setText(R.id.tv_item_circle_classify_join,"加入"+item.joinUserCount)
        helper.setText(R.id.tv_item_circle_classify_topic,"话题"+item.qaCount)
        helper.setText(R.id.tv_item_circle_classify_type,item.classifytitle)

        if (item.endServiceDistanceTime!=""&&item.endServiceDistanceTime.isNotEmpty()){
            helper.setText(R.id.time_to_end,item.endServiceDistanceTime)
        }


        val checkBox = helper.getView<CheckBox>(R.id.cb_item_select_join_circle2)
        if (item.hasJoin){
            checkBox.text = "进圈"
            checkBox.setTextColor(mContext.getColorForKotlin(R.color.button_blue))
            helper.addOnClickListener(R.id.cb_item_select_join_circle2)
            helper.addOnClickListener(R.id.iv_item_circle_classify_icon)
            helper.addOnClickListener(R.id.tv_item_circle_classify_title)
            helper.addOnClickListener(R.id.tv_item_circle_classify_desc)
            helper.addOnClickListener(R.id.con_click)
        }else{
            checkBox.text = "续费"
            checkBox.setTextColor(mContext.getColorForKotlin(R.color.red))
            helper.addOnClickListener(R.id.cb_item_select_join_circle2)
            helper.addOnClickListener(R.id.iv_item_circle_classify_icon)
            helper.addOnClickListener(R.id.tv_item_circle_classify_title)
            helper.addOnClickListener(R.id.tv_item_circle_classify_desc)
            helper.addOnClickListener(R.id.con_click)
        }

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