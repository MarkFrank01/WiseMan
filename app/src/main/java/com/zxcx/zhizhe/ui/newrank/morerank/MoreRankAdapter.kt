package com.zxcx.zhizhe.ui.newrank.morerank

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.getColorForKotlin

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankAdapter(data: List<UserRankBean>) : BaseQuickAdapter<UserRankBean, BaseViewHolder>(R.layout.item_more_rank_user, data) {

    override fun convert(helper: BaseViewHolder, item: UserRankBean) {
        val ranknum = helper.getView<TextView>(R.id.tv_item_rank_user_rank)
        ranknum.text = item.rankIndex.toString()

        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_rank_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)

        helper.setText(R.id.tv_user_name, item.name)
        helper.setText(R.id.tv_user_rank_level, mContext.getString(R.string.tv_level, item.level))

        helper.setText(R.id.tv_item_rank_num_zl, item.intelligence.toString())

        when (item.followType) {
            0 -> {
                helper.setText(R.id.cb_item_search_user_follow,"关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.button_blue))
                helper.setChecked(R.id.cb_item_search_user_follow,false)
            }

            1->{
                helper.setText(R.id.cb_item_search_user_follow,"已关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.text_color_3))
                helper.setChecked(R.id.cb_item_search_user_follow,true)

            }

            2->{
                helper.setText(R.id.cb_item_search_user_follow,"互相关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.text_color_3))
                helper.setChecked(R.id.cb_item_search_user_follow,true)

            }
        }

        helper.addOnClickListener(R.id.cb_item_search_user_follow)
    }
}