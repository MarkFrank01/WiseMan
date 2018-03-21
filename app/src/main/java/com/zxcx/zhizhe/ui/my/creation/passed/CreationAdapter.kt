package com.zxcx.zhizhe.ui.search.result.card

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class CreationAdapter(data : List<CreationBean>) : BaseQuickAdapter<CreationBean, BaseViewHolder>(R.layout.item_card,data){

    override fun convert(helper: BaseViewHolder, item: CreationBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        val para = imageView.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px((20 * 2).toFloat())) * 9 / 16
        imageView.layoutParams = para

        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_card_bag, item.cardBagName)
        helper.setText(R.id.tv_item_card_like, item.likeNum.toString())
        helper.setText(R.id.tv_item_card_read, item.readNum.toString())
        when (item.cardType) {
            1 -> helper.setText(R.id.tv_item_card_type, "卡片")
            2 -> helper.setText(R.id.tv_item_card_type, "长文")
        }

    }

}