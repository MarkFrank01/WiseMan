package com.zxcx.zhizhe.ui.my.creation.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 * 不可删除作品Adapter
 */
//class CreationAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_other_user_creation, data) {
class CreationAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(data) {

    /**
     * 特别注意此处的layout只有两种类型，
     * 1,2复用一个layout
     * 3 单独使用一个layout
     */
    init {
        addItemType(CardBean.Article, R.layout.item_other_user_creation)
        addItemType(CardBean.Article_LONG, R.layout.item_other_user_creation)
        addItemType(CardBean.Article_LINK, R.layout.item_link_creation)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {


        when (helper.itemViewType) {
        //多类型item加入此选择即可
            CardBean.Article, CardBean.Article_LONG -> {
                val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
                val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
                ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

                helper.setText(R.id.tv_item_card_title, item.name)
                helper.setText(R.id.tv_item_card_category, item.categoryName)
                if (item.labelName!=""&&item.labelName.isNotEmpty()) {
                    helper.setText(R.id.tv_item_card_label, item.getLabelName())
                }else{
                    helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
                }
//                if (item.secondCollectionTitle!=""&&item.secondCollectionTitle.isNotEmpty()) {
//                    helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
//                    helper.setText(R.id.tv_item_card_label2,item.getSecondLabelName())
//                }
                helper.setText(R.id.tv_item_card_read, item.readNum.toString())
                helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

                imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                        R.string.card_title_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                        R.string.card_category_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                        R.string.card_label_transition_name)

                helper.setText(R.id.tv_item_card_time,item.distanceTime)
            }

            CardBean.Article_LINK -> {
                helper.setText(R.id.tv_item_card_link,item.content)
                        .setText(R.id.tv_item_card_title,item.name)
                        .setText(R.id.tv_item_link_time,item.distanceTime)
            }
        }
    }
}