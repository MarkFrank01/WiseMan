package com.zxcx.zhizhe.ui.card.hot

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyAdapter(data:MutableList<CardBean>):
        BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_daily_card,data){

//    private lateinit var mItemAdapter : DailyItemAdapter

    override fun convert(helper: BaseViewHolder, item: CardBean) {

        if (item.title.isNotEmpty()||item.title!="") {
            helper.getView<TextView>(R.id.tv_daily_title).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.tv_daily_title).text = item.title
        }

        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_category, item.categoryName)
        helper.setText(R.id.tv_item_card_read, item.readNum.toString())
        helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

        if (item.labelName!=""||item.labelName.isNotEmpty()) {
            helper.setText(R.id.tv_item_card_label, item.getLabelName())
        }else{
            helper.getView<TextView>(R.id.tv_item_card_label).visibility = View.GONE
        }

        if (item.secondCollectionTitle != "" && item.secondCollectionTitle.isNotEmpty()) {
            helper.getView<TextView>(R.id.tv_item_card_label2).visibility = View.VISIBLE
            helper.setText(R.id.tv_item_card_label2, item.getSecondLabelName())
        }

        imageView.transitionName = mContext.getString(R.string.card_img_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                R.string.card_title_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                R.string.card_category_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                R.string.card_label_transition_name, helper.adapterPosition)


//        helper.setText(R.id.tv_daily_title,item.title)
//
//        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
//
//        mItemAdapter = DailyItemAdapter(arrayListOf())
//
//        val rv_item = helper.getView<RecyclerView>(R.id.item_daily_detail_card)
//        rv_item.layoutManager = layoutManager
//        rv_item.adapter = mItemAdapter
//        mItemAdapter.setNewData(item.articleVOArrayList)
    }

}