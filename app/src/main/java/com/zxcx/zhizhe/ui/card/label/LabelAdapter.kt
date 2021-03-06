package com.zxcx.zhizhe.ui.card.label

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean
import com.zxcx.zhizhe.ui.article.SubjectItemArticleAdapter
import com.zxcx.zhizhe.ui.article.SubjectOnClickListener
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

//标记参考
class LabelAdapter(data: List<ArticleAndSubjectBean>?, private val mListener: SubjectOnClickListener) :
		BaseMultiItemQuickAdapter<ArticleAndSubjectBean, BaseViewHolder>(data), BaseQuickAdapter.OnItemClickListener {

	var layoutManager: LinearLayoutManager

	init {
		addItemType(ArticleAndSubjectBean.TYPE_ARTICLE, R.layout.item_other_user_creation)
		addItemType(ArticleAndSubjectBean.TYPE_SUBJECT, R.layout.item_label_subject)
		layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
	}

	override fun convert(helper: BaseViewHolder, item: ArticleAndSubjectBean) {
		when (helper.itemViewType) {
			ArticleAndSubjectBean.TYPE_ARTICLE -> initArticleView(helper, item)
			ArticleAndSubjectBean.TYPE_SUBJECT -> initSubjectView(helper, item)
		}
	}

	private fun initArticleView(helper: BaseViewHolder, bean: ArticleAndSubjectBean) {
		val item = bean.cardBean
		val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
		val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
		ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

		helper.setText(R.id.tv_item_card_title, item.name)
		helper.setText(R.id.tv_item_card_category, item.categoryName)
		helper.setText(R.id.tv_item_card_label, item.getLabelName())
		helper.setText(R.id.tv_item_card_read, item.readNum.toString())
		helper.setText(R.id.tv_item_card_comment, item.commentNum.toString())
        helper.setText(R.id.tv_item_card_time,item.distanceTime)
	}

	private fun initSubjectView(helper: BaseViewHolder, bean: ArticleAndSubjectBean) {
		val subjectBean = bean.subjectBean
		helper.setText(R.id.tv_item_subject_name, subjectBean.name)
		helper.setText(R.id.tv_item_subject_category, subjectBean.categoryName)
		helper.setText(R.id.tv_item_subject_label, subjectBean.getLabelName())
		helper.itemView.setOnClickListener {
			mListener.subjectOnClick(subjectBean)
		}
		var cardList = subjectBean.cardList
		if (cardList == null || cardList.isEmpty()) return
		if (cardList.size > 3) cardList = cardList.subList(0, 3)
		val recyclerView = helper.getView<RecyclerView>(R.id.rv_item_subject_article)
		recyclerView.layoutManager = layoutManager
		val adapter = SubjectItemArticleAdapter(cardList)
		adapter.onItemClickListener = this
		recyclerView.adapter = adapter

	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
		mListener.articleOnClick(adapter.data[position] as CardBean)
	}
}
