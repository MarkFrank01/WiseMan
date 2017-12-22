package com.zxcx.zhizhe.ui.home.rank

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
class RankAdapter(data : List<UserRankBean>) : BaseQuickAdapter<UserRankBean, BaseViewHolder>(R.layout.item_rank_user,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: UserRankBean) {
        val title = helper.getView<TextView>(R.id.tv_item_rank_user_name)
        TextViewUtils.setTextViewColorAndBold(title, mKeyword, item.name)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_rank_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        helper.setText(R.id.tv_item_rank_user_card, (item.cardNum?:0).toString())
        helper.setText(R.id.tv_item_rank_user_fans, (item.fansNum?:0).toString())
        helper.setText(R.id.tv_item_rank_user_read, (item.readNum?:0).toString())
        helper.setVisible(R.id.view_line,helper.adapterPosition != itemCount-1)
        showRank(helper,item)
    }

    private fun showRank(helper: BaseViewHolder, bean: UserRankBean) {
        when (bean.rankIndex) {
            1 -> {
            }
            2 -> {
                helper.setVisible(R.id.tv_rank_my_header_rank, false)
                helper.setVisible(R.id.tv_rank_my_no_rank, false)
                helper.setImageResource(R.id.iv_rank_my_header_rank,R.drawable.rank_2)
            }
            3 -> {
                helper.setVisible(R.id.tv_rank_my_header_rank, false)
                helper.setVisible(R.id.tv_rank_my_no_rank, false)
                helper.setImageResource(R.id.iv_rank_my_header_rank,R.drawable.rank_3)
            }
            in 4..99 -> {
                helper.setVisible(R.id.tv_rank_my_no_rank, false)
                helper.setVisible(R.id.tv_rank_my_header_rank, true)
                helper.setText(R.id.tv_rank_my_header_rank,bean.rankIndex.toString())
                helper.setImageResource(R.id.iv_rank_my_header_rank,R.drawable.rank_4)
            }
            else -> {
                helper.setVisible(R.id.tv_rank_my_no_rank, true)
                helper.setVisible(R.id.fl_rank_my_header_rank, false)
            }
        }
    }

}