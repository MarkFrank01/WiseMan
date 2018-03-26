package com.zxcx.zhizhe.ui.my.likeCards

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 */
class MyCardsAdapter(data : List<MyCardsBean>) : BaseQuickAdapter<MyCardsBean, BaseViewHolder>(R.layout.item_my_card,data){

    lateinit var mListener: SwipeMenuClickListener

    init {
        setHeaderAndEmpty(true)
    }

    override fun convert(helper: BaseViewHolder, item: MyCardsBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)

        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_card_bag, item.cardBagName)
        helper.setText(R.id.tv_item_card_reade_num, item.readNum.toString())
        helper.setText(R.id.tv_item_card_collect_num, item.collectNum.toString())
        when (item.cardType) {
            1 -> helper.setText(R.id.tv_item_card_type, "卡片")
            2 -> helper.setText(R.id.tv_item_card_type, "长文")
        }

        val easySwipeMenuLayout = helper.getView<EasySwipeMenuLayout>(R.id.es)
        helper.getView<TextView>(R.id.tv_cancel).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
        }
        helper.getView<TextView>(R.id.tv_delete).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
            mListener.onDeleteClick(mData.indexOf(item))
            remove(mData.indexOf(item))
        }
        helper.getView<FrameLayout>(R.id.content_view).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
            mListener.onContentClick(mData.indexOf(item))
        }
    }

}