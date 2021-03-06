package com.zxcx.zhizhe.ui.card.cardDetails

import android.support.constraint.ConstraintLayout
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pixplicity.htmlcompat.HtmlCompat
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.expandViewTouchDelegate

/**
 * Created by anm on 2017/6/26.
 */

class CardDetailsAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_card_details, data) {

    override fun convert(helper: BaseViewHolder, item: CardBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_details_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)
        helper.setText(R.id.tv_item_card_details_title, item.name)
        helper.setText(R.id.tv_item_card_details_category, item.categoryName)
//        helper.setText(R.id.tv_item_card_details_label, item.getLabelName())
        helper.setText(R.id.tv_item_card_details_author, item.authorName)
        helper.setText(R.id.tv_item_card_details_comment, item.commentNum.toString())
        helper.setText(R.id.tv_item_card_details_collect, item.collectNum.toString())
        helper.setText(R.id.tv_item_card_details_like, item.likeNum.toString())
        helper.setChecked(R.id.cb_item_card_details_follow, item.isFollow)
        helper.setChecked(R.id.cb_item_card_details_collect, item.isCollect)
        helper.setChecked(R.id.cb_item_card_details_like, item.isLike)
        helper.getView<TextView>(R.id.tv_item_card_details_collect).isEnabled = item.isCollect
        helper.getView<TextView>(R.id.tv_item_card_details_like).isEnabled = item.isLike

        helper.setText(R.id.tv_item_card_details_time,item.distanceTime)

        helper.setText(R.id.tv_xf,item.relatedCircleTitle)



        if (item.showOther1){
            helper.getView<ConstraintLayout>(R.id.show_1).visibility = View.VISIBLE
            helper.getView<ConstraintLayout>(R.id.show_2).visibility = View.GONE
        }else{
            helper.getView<ConstraintLayout>(R.id.show_1).visibility = View.GONE
            helper.getView<ConstraintLayout>(R.id.show_2).visibility = View.VISIBLE
        }

        if (item.showOther2){
            helper.getView<ConstraintLayout>(R.id.show_2).visibility = View.VISIBLE
        }else{
            helper.getView<ConstraintLayout>(R.id.show_2).visibility = View.GONE
            helper.getView<ConstraintLayout>(R.id.show_1).visibility = View.VISIBLE
        }

        if (item.relatedCircleTitle.isEmpty()||item.relatedCircleTitle==""){
            helper.getView<ConstraintLayout>(R.id.show_1).visibility = View.GONE
        }

        if(item.labelName!=""&&item.labelName.isNotEmpty()) {
            helper.getView<TextView>(R.id.tv_item_card_details_label).visibility = View.VISIBLE
            helper.setText(R.id.tv_item_card_details_label, item.getLabelName())
        }else{
            helper.getView<TextView>(R.id.tv_item_card_details_label).visibility = View.GONE
        }


        if (item.secondCollectionTitle!=""&&item.secondCollectionTitle.isNotEmpty()){
            helper.getView<TextView>(R.id.tv_item_card_details_label2).visibility = View.VISIBLE
            helper.setText(R.id.tv_item_card_details_label2,item.getSecondLabelName())
        }

        try {
            val fromHtml = HtmlCompat.fromHtml(mContext, item.content, 0)
            val tvContent = helper.getView<TextView>(R.id.tv_item_card_details_content)
            tvContent.movementMethod = LinkMovementMethod.getInstance()
            tvContent.text = fromHtml
        } catch (e: Exception) {

        }

        //设置transitionName
        imageView.transitionName = mContext.getString(R.string.card_img_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_details_title).transitionName = mContext.getString(
                R.string.card_title_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_details_category).transitionName = mContext.getString(
                R.string.card_category_transition_name, helper.adapterPosition)
        helper.getView<TextView>(R.id.tv_item_card_details_label).transitionName = mContext.getString(
                R.string.card_label_transition_name, helper.adapterPosition)

        //扩大10dp点击区域
        helper.getView<View>(R.id.cb_item_card_details_follow).expandViewTouchDelegate(ScreenUtils.dip2px(8f))
        helper.getView<View>(R.id.iv_item_card_details_comment).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        helper.getView<View>(R.id.cb_item_card_details_collect).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        helper.getView<View>(R.id.cb_item_card_details_like).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        helper.getView<View>(R.id.iv_item_card_details_share).expandViewTouchDelegate(ScreenUtils.dip2px(10f))

        //点击事件
        helper.addOnClickListener(R.id.tv_item_card_details_label)
        helper.addOnClickListener(R.id.tv_item_card_details_author)
        helper.addOnClickListener(R.id.tv_item_card_details_goto_ad)
        helper.addOnClickListener(R.id.cb_item_card_details_follow)
        helper.addOnClickListener(R.id.iv_item_card_details_comment)
        helper.addOnClickListener(R.id.cb_item_card_details_collect)
        helper.addOnClickListener(R.id.cb_item_card_details_like)
        helper.addOnClickListener(R.id.iv_item_card_details_share)

        helper.addOnClickListener(R.id.show_1)
        helper.addOnClickListener(R.id.show_2)

        //是否广告
        helper.setGone(R.id.tv_item_card_details_author, item.adUrl.isEmpty())
        helper.setGone(R.id.cb_item_card_details_follow, item.adUrl.isEmpty())
        helper.setGone(R.id.tv_item_card_details_goto_ad, item.adUrl.isNotEmpty())

//        Log.e("Type", "type:" + item.authorType)
        if (item.authorType != 0 && item.cardType == 1) {
            helper.getView<ImageView>(R.id.iv_item_card_officials).visibility = View.VISIBLE
        }


    }
}