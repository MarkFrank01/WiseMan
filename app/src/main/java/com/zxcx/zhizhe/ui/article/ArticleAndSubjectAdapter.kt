package com.zxcx.zhizhe.ui.article

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * 长文及专题混杂的Adapter
 */

class ArticleAndSubjectAdapter(data: List<ArticleAndSubjectBean>?, private val mListener: SubjectOnClickListener) :
        BaseMultiItemQuickAdapter<ArticleAndSubjectBean, BaseViewHolder>(data) {

    init {
        addItemType(ArticleAndSubjectBean.TYPE_ARTICLE, R.layout.item_article)
        addItemType(ArticleAndSubjectBean.TYPE_SUBJECT, R.layout.item_subject)
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

        helper.setText(R.id.tv_item_card_time, item.distanceTime)

        imageView.transitionName = mContext.getString(R.string.card_img_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_title).transitionName = mContext.getString(
                R.string.card_title_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_category).transitionName = mContext.getString(
                R.string.card_category_transition_name)
        helper.getView<TextView>(R.id.tv_item_card_label).transitionName = mContext.getString(
                R.string.card_label_transition_name)
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
        if (recyclerView.layoutManager == null) {
            val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
        }
        val adapter = SubjectItemArticleAdapter(cardList)
        adapter.setOnItemClickListener { adapter, view, position ->
            mListener.subjectOnClick(subjectBean)
        }
        recyclerView.adapter = adapter
    }
}
