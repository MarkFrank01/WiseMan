package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * Created by anm on 2017/12/1.
 * 可删除作品Adapter
 */
//class DeleteCreationAdapter(data: List<CardBean>) : BaseQuickAdapter<CardBean, BaseViewHolder>(R.layout.item_my_card, data) {
class OwnerManageAdapter(data: List<CardBean>) : BaseMultiItemQuickAdapter<CardBean, BaseViewHolder>(data) {

    init {
        addItemType(CardBean.Article,R.layout.item_my_card_manage)
        addItemType(CardBean.Article_LONG,R.layout.item_my_card_manage)
        addItemType(CardBean.Article_LINK,R.layout.item_link_creation_new)
    }

    lateinit var mListener: SwipeMenuClickListener2
//    lateinit var mListener1: DeleteLinkListener2

    override fun convert(helper: BaseViewHolder, item: CardBean) {

        when(helper.itemViewType){
            CardBean.Article,CardBean.Article_LONG ->{
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
                helper.setText(R.id.tv_item_card_read, item.readNum.toString())
                helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())

                imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                        R.string.card_title_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                        R.string.card_category_transition_name)
                helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                        R.string.card_label_transition_name)

                val easySwipeMenuLayout = helper.getView<EasySwipeMenuLayout>(R.id.es)
                helper.getView<View>(R.id.iv_delete).setOnClickListener {
                    easySwipeMenuLayout.resetStatus()
                    mListener.onDeleteClick(mData.indexOf(item))
                }
                helper.getView<View>(R.id.content_view).setOnClickListener {
                    easySwipeMenuLayout.resetStatus()
                    mListener.onContentClick(mData.indexOf(item))
                }
                helper.getView<View>(R.id.iv_top).setOnClickListener {
                    easySwipeMenuLayout.resetStatus()
                    mListener.onTopClick(mData.indexOf(item))
                }

                helper.setText(R.id.tv_item_card_time,item.distanceTime)
            }

//            CardBean.Article_LINK -> {
//                helper.setText(R.id.tv_item_card_link,item.content)
//                        .setText(R.id.tv_item_card_title,item.name)
//                        .setText(R.id.tv_item_link_time,item.distanceTime)
//
//                val easySwipeMenuLayout = helper.getView<EasySwipeMenuLayout>(R.id.es)
//                helper.getView<View>(R.id.iv_delete).setOnClickListener {
//                    easySwipeMenuLayout.resetStatus()
////                    mListener.onDeleteClick(mData.indexOf(item))
//                    mListener1.DeleteLink(mData.indexOf(item))
//                }
//                helper.getView<View>(R.id.content_view).setOnClickListener {
//                    easySwipeMenuLayout.resetStatus()
//                    mListener.onContentClick(mData.indexOf(item))
//                }
//            }
        }


    }

}