package com.zxcx.zhizhe.ui.comment

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.*


class CommentAdapter(data: MutableList<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

	var userId = 0

	init {
		addItemType(CommentBean.TYPE_LEVEL_0, R.layout.item_comment)
		addItemType(ChildCommentBean.TYPE_LEVEL_1, R.layout.item_child_comment)
	}

	override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
		when (helper.itemViewType) {
			CommentBean.TYPE_LEVEL_0 -> initCommentView(helper, item)
			ChildCommentBean.TYPE_LEVEL_1 -> initChildCommentView(helper, item)
		}
	}

	private fun initCommentView(helper: BaseViewHolder, bean: MultiItemEntity) {
		val item = bean as CommentBean
		val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.userImageUrl)
        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)

        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType == 1){
            imageVIP.visibility = View.VISIBLE
        }

        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
		helper.setText(R.id.tv_item_comment_name, item.userName)
        helper.setText(R.id.tv_item_comment_distanceTime,item.distanceTime)
		helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())
		helper.setText(R.id.tv_item_comment_content, item.content)
		helper.setChecked(R.id.cb_item_comment_like, item.hasLike)
		helper.getView<TextView>(R.id.tv_item_comment_like_num).isEnabled = item.hasLike
		helper.addOnClickListener(R.id.cb_item_comment_like)
		helper.getView<CheckBox>(R.id.cb_item_comment_like).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
		when {
			item.articleAuthor -> {
				helper.setGone(R.id.tv_item_comment_flag, true)
				helper.setText(R.id.tv_item_comment_flag, "作者")
			}
			item.userId == userId -> {
				helper.setGone(R.id.tv_item_comment_flag, true)
				helper.setText(R.id.tv_item_comment_flag, "自己")
			}
			else -> {
				helper.setGone(R.id.tv_item_comment_flag, false)
			}
		}
		helper.setGone(R.id.tv_item_comment_expand, item.childCommentList.isNotEmpty())
		val tvExpand = helper.getView<TextView>(R.id.tv_item_comment_expand)
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
	}

	private fun initChildCommentView(helper: BaseViewHolder, bean: MultiItemEntity) {
		val item = bean as ChildCommentBean
		val imageView = helper.getView<ImageView>(R.id.iv_item_comment)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.userImageUrl)

        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)

        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType == 1){
            imageVIP.visibility = View.VISIBLE
        }

		ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
		helper.setText(R.id.tv_item_comment_name, item.userName)
        helper.setText(R.id.tv_item_comment_distanceTime,item.distanceTime)
		helper.setText(R.id.tv_item_comment_like_num, item.likeCount.toString())
		helper.setText(R.id.tv_item_comment_content, item.content)
		helper.setChecked(R.id.cb_item_comment_like, item.hasLike)
		helper.getView<TextView>(R.id.tv_item_comment_like_num).isEnabled = item.hasLike
		helper.addOnClickListener(R.id.cb_item_comment_like)
		helper.getView<CheckBox>(R.id.cb_item_comment_like).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
		when {
			item.articleAuthor -> {
				helper.setGone(R.id.tv_item_comment_flag, true)
				helper.setText(R.id.tv_item_comment_flag, "作者")
			}
			item.userId == userId -> {
				helper.setGone(R.id.tv_item_comment_flag, true)
				helper.setText(R.id.tv_item_comment_flag, "自己")
			}
			else -> {
				helper.setGone(R.id.tv_item_comment_flag, false)
			}
		}
	}
}
