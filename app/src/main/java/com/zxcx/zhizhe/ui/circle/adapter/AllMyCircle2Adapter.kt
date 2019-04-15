package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.getColorForKotlin

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCircle2Adapter(data: List<CircleBean>) :
        BaseMultiItemQuickAdapter<CircleBean, BaseViewHolder>(data) {

    init {
        addItemType(CircleBean.CIRCLE_HOME_1, R.layout.item_circle_classify_create)
    }

    override fun convert(helper: BaseViewHolder, item: CircleBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_circle_classify_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.titleImage)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_circle_classify_title, item.title)
        helper.setText(R.id.tv_item_circle_classify_desc, item.sign)
        helper.setText(R.id.tv_item_circle_classify_join, "加入" + item.joinUserCount)
        helper.setText(R.id.tv_item_circle_classify_topic, "话题" + item.qaCount)
        helper.setText(R.id.tv_item_circle_classify_type,item.classifytitle)

        LogCat.e("?????"+item.circleExpiredDistanceTime)
        if (item.circleExpiredDistanceTime!=""&&item.circleExpiredDistanceTime.isNotEmpty()) {
            helper.setText(R.id.time_to_chuli, "剩余" + item.circleExpiredDistanceTime + "天")
        }


//        helper.getView<CheckBox>(R.id.cb_item_select_join_circle2).visibility = View.GONE

        when (item.statusType) {
            -3 -> {
                helper.setText(R.id.cb_item_select_join_circle2, "已关闭")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.red))
            }

            -2 -> {
                helper.setText(R.id.cb_item_select_join_circle2, "未通过")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.text_color_3))
            }

            -1 -> {
                //  helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))

                helper.setText(R.id.cb_item_select_join_circle2, "待提交")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.red))
            }

            0 -> {
                helper.setText(R.id.cb_item_select_join_circle2, "审核中")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.text_color_3))
            }

            1 -> {
                helper.setText(R.id.cb_item_select_join_circle2, "限免中")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.button_blue))
                helper.addOnClickListener(R.id.cb_item_select_join_circle2)

            }

            2 -> {
//                helper.setText(R.id.tv_statue, "已通过")
//                        .setTextColor(R.id.tv_statue, mContext.getColorForKotlin(R.color.text_color_3))
                helper.setText(R.id.cb_item_select_join_circle2, "已上线")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.button_blue))
            }

            3 -> {
                helper.setText(R.id.cb_item_select_join_circle2, "再编辑审核中")
                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.text_color_3))
            }

//            4->{
//                helper.setText(R.id.cb_item_select_join_circle2, "续费")
//                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.red))
//            }
//
//            5->{
//                helper.setText(R.id.cb_item_select_join_circle2, "进圈")
//                        .setTextColor(R.id.cb_item_select_join_circle2, mContext.getColorForKotlin(R.color.button_blue))
//            }
        }

        helper.addOnClickListener(R.id.cb_item_select_join_circle2)


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