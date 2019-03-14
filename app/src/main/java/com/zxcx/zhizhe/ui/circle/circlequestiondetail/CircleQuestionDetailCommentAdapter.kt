package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.*


class CircleQuestionDetailCommentAdapter(data: MutableList<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    var userId = 0

    init {
        addItemType(CircleCommentBean.TYPE_LEVEL_0, R.layout.item_comment_circle)
        addItemType(CircleChildCommentBean.TYPE_LEVEL_1, R.layout.item_child_comment_circle)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            CircleCommentBean.TYPE_LEVEL_0 -> initCommentView(helper, item)
            CircleChildCommentBean.TYPE_LEVEL_1 -> initChildCommentView(helper, item)
        }
    }

    private fun initCommentView(helper: BaseViewHolder, bean: MultiItemEntity) {

        //新
        val item = bean as CircleCommentBean
        val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.authorVO?.avatar)
        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)

        //加载评论的头像
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        //人头名
        helper.setText(R.id.tv_item_comment_name, item.authorVO?.name)
        //时间
        helper.setText(R.id.tv_item_comment_distanceTime, item.createTime)

        //内容
        helper.setText(R.id.tv_item_comment_content, item.description)

        //点赞数
        helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())

        //子项
        helper.setGone(R.id.tv_item_comment_expand, item.childQaCommentVOList.isNotEmpty())
        val tvExpand = helper.getView<TextView>(R.id.tv_item_comment_expand)
        tvExpand.text = "共"+item.childCommentCount+"条回复"
        tvExpand.setTextColor(mContext.getColorForKotlin(R.color.button_blue))

        if (item.isExpanded) {
            TextViewUtils.setTextRightDrawable(mContext, R.drawable.common_collapse, tvExpand)
        } else {
            TextViewUtils.setTextRightDrawable(mContext, R.drawable.common_expand, tvExpand)
        }
        tvExpand.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        tvExpand.setOnClickListener {
            val pos = helper.adapterPosition
            if (item.isExpanded) {
                collapse(pos)
            } else {
                expand(pos)
            }
        }

        val sixPhoto = helper.getView<BGANinePhotoLayout>(R.id.six_photo_widget)
        if (item.qacImageList.size>0){
            sixPhoto.visibility = View.VISIBLE
        }
        sixPhoto.data = item.qacImageList

//		val item = bean as CircleCommentBean
//		val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
//		val imageUrl = ZhiZheUtils.getHDImageUrl(item.userImageUrl)
//        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)
//
//        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType == 1){
//            imageVIP.visibility = View.VISIBLE
//        }
//
//        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
//		helper.setText(R.id.tv_item_comment_name, item.userName)
//        helper.setText(R.id.tv_item_comment_distanceTime,item.distanceTime)
//		helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())
//		helper.setText(R.id.tv_item_comment_content, item.content)
//		helper.setChecked(R.id.cb_item_comment_like, item.hasLike)
//		helper.getView<TextView>(R.id.tv_item_comment_like_num).isEnabled = item.hasLike
//		helper.addOnClickListener(R.id.cb_item_comment_like)
//		helper.getView<CheckBox>(R.id.cb_item_comment_like).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
//		when {
//			item.articleAuthor -> {
//				helper.setGone(R.id.tv_item_comment_flag, true)
//				helper.setText(R.id.tv_item_comment_flag, "作者")
//			}
//			item.userId == userId -> {
//				helper.setGone(R.id.tv_item_comment_flag, true)
//				helper.setText(R.id.tv_item_comment_flag, "自己")
//			}
//			else -> {
//				helper.setGone(R.id.tv_item_comment_flag, false)
//			}
//		}
//		helper.setGone(R.id.tv_item_comment_expand, item.childCommentList.isNotEmpty())
//		val tvExpand = helper.getView<TextView>(R.id.tv_item_comment_expand)
//		if (item.isExpanded) {
//			TextViewUtils.setTextRightDrawable(mContext, R.drawable.common_collapse, tvExpand)
//		} else {
//			TextViewUtils.setTextRightDrawable(mContext, R.drawable.common_expand, tvExpand)
//		}
//		tvExpand.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
//		tvExpand.setOnClickListener {
//			val pos = helper.adapterPosition
//			if (item.isExpanded) {
//				collapse(pos)
//			} else {
//				expand(pos)
//			}
//		}
    }

    private fun initChildCommentView(helper: BaseViewHolder, bean: MultiItemEntity) {
        val item = bean as CircleChildCommentBean
        val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.authorVO?.avatar)

        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)

        //人头
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        //人头名
        helper.setText(R.id.tv_item_comment_name, item.authorVO?.name)
        //时间
        helper.setText(R.id.tv_item_comment_distanceTime,item.createTime)

        //内容
        helper.setText(R.id.tv_item_comment_content, item.description)

        //点赞数
        helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())

//		val item = bean as CircleChildCommentBean
//		val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
//		val imageUrl = ZhiZheUtils.getHDImageUrl(item.userImageUrl)
//
//        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)
//
//        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType == 1){
//            imageVIP.visibility = View.VISIBLE
//        }
//
//		ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
//		helper.setText(R.id.tv_item_comment_name, item.userName)
//        helper.setText(R.id.tv_item_comment_distanceTime,item.distanceTime)
//		helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())
//		helper.setText(R.id.tv_item_comment_content, item.content)
//		helper.setChecked(R.id.cb_item_comment_like, item.hasLike)
//		helper.getView<TextView>(R.id.tv_item_comment_like_num).isEnabled = item.hasLike
//		helper.addOnClickListener(R.id.cb_item_comment_like)
//		helper.getView<CheckBox>(R.id.cb_item_comment_like).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
//		when {
//			item.articleAuthor -> {
//				helper.setGone(R.id.tv_item_comment_flag, true)
//				helper.setText(R.id.tv_item_comment_flag, "作者")
//			}
//			item.userId == userId -> {
//				helper.setGone(R.id.tv_item_comment_flag, true)
//				helper.setText(R.id.tv_item_comment_flag, "自己")
//			}
//			else -> {
//				helper.setGone(R.id.tv_item_comment_flag, false)
//			}
//		}
    }
}
