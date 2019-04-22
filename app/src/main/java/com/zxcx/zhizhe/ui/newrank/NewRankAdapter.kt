package com.zxcx.zhizhe.ui.newrank

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankAdapter(data:List<UserRankBean>):BaseQuickAdapter<UserRankBean,BaseViewHolder>(R.layout.item_new_rank_user,data) {

    override fun convert(helper: BaseViewHolder, item: UserRankBean) {
        val title = helper.getView<TextView>(R.id.tv_item_rank_user_rank)
        title.text = item.rankIndex.toString()

        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_rank_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext,imageUrl,R.drawable.default_header,imageView)

        helper.setText(R.id.tv_item_user_name,item.name)
        helper.setText(R.id.tv_item_rank_num_zl,item.intelligence.toString())

        helper.addOnClickListener(R.id.con_la)
    }
}