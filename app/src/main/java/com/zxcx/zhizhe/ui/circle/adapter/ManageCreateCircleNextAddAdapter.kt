package com.zxcx.zhizhe.ui.circle.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils


//class CreationAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_other_user_creation, data) {
class ManageCreateCircleNextAddAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(data) {

    /**
     * 特别注意此处的layout只有两种类型，
     * 1,2复用一个layout
     * 3 单独使用一个layout
     */
    init {
        addItemType(CardBean.Article, R.layout.item_other_user_creation_card)
        addItemType(CardBean.Article_LONG, R.layout.item_other_user_creation_card)
//        addItemType(CardBean.Article_LINK, R.layout.item_link_creation)
    }

    override fun convert(helper: BaseViewHolder, item: CardBean) {
                if (item.showTitle!=""&&item.showTitle.isNotEmpty()){
                    helper.getView<TextView>(R.id.tv_daily_title).visibility = View.VISIBLE
                    helper.setText(R.id.tv_daily_title,item.showTitle)
                }else{
                    helper.getView<TextView>(R.id.tv_daily_title).visibility = View.GONE
                }

                if (item.showNumTitle!=""&&item.showNumTitle.isNotEmpty()){
                    helper.getView<TextView>(R.id.tv_daily_title_num).visibility = View.VISIBLE
                    helper.setText(R.id.tv_daily_title_num,item.showNumTitle)
                }else{
                    helper.getView<TextView>(R.id.tv_daily_title_num).visibility = View.GONE

                }

//        when (helper.itemViewType) {
        //多类型item加入此选择即可
//            CardBean.Article, CardBean.Article_LONG -> {
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

                helper.addOnClickListener(R.id.cb_choose_push_manage)



//                val cb = helper.getView<CheckBox>(R.id.cb_choose_push_manage)
//                if (!item.circleFix!!){
//                    cb.isChecked = false
//                    helper.addOnClickListener(R.id.cb_choose_push_manage)
//                }else{
//
//                    cb.visibility = View.GONE
//
//                    helper.setTextColor(R.id.tv_item_card_title,mContext.getColorForKotlin(R.color.text_color_d2))
//                    helper.setTextColor(R.id.tv_item_card_category,mContext.getColorForKotlin(R.color.text_color_d2))
//                    helper.setTextColor(R.id.tv_item_card_label,mContext.getColorForKotlin(R.color.text_color_d2))
//                    helper.setTextColor(R.id.tv_item_card_time,mContext.getColorForKotlin(R.color.text_color_d2))
//
//
//                    helper.addOnClickListener(R.id.con)
//                    helper.setTextColor(R.id.tv_item_card_read,mContext.getColorForKotlin(R.color.text_color_d2))
//                    helper.setTextColor(R.id.tv_item_card_comment,mContext.getColorForKotlin(R.color.text_color_d2))
//                }


    }
}