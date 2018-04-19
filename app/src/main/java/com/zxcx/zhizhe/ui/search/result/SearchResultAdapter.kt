package com.zxcx.zhizhe.ui.search.result

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.WrapContentHeightViewPager
import kotlinx.android.synthetic.main.item_hot_subject_card.view.*

/**
 * Created by anm on 2017/12/1.
 */
class SearchResultAdapter(data : List<SearchResultBean>, private val mListener: SubjectOnClickListener)
    : BaseMultiItemQuickAdapter<SearchResultBean, BaseViewHolder>(data){

    var mKeyword = ""

    init {
        addItemType(SearchResultBean.TYPE_CARD, R.layout.item_search_result)
        addItemType(SearchResultBean.TYPE_SUBJECT, R.layout.item_subject)
    }

    override fun convert(helper: BaseViewHolder, item: SearchResultBean) {
        when (helper.itemViewType) {
            SearchResultBean.TYPE_CARD -> initCardView(helper, item)
            SearchResultBean.TYPE_SUBJECT -> initCardBagView(helper, item)
        }

    }

    private fun initCardView(helper: BaseViewHolder, bean: SearchResultBean) {
        helper.addOnClickListener(R.id.tv_item_search_result_card_bag)
        val item = bean.cardBean
        val title = helper.getView<TextView>(R.id.tv_item_search_result_title)
        val content = helper.getView<TextView>(R.id.tv_item_search_result_content)
        TextViewUtils.setTextViewColor(title, mKeyword, item.name)
        TextViewUtils.setTextViewColor(content, mKeyword, item.content)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_search_result_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)
        helper.setText(R.id.tv_item_search_result_card_bag, item.cardBagName)
        helper.setText(R.id.tv_item_search_result_collect, item.collectNum.toString() + "")
        helper.setText(R.id.tv_item_search_result_read, item.readNum.toString() + "")
        when (item.cardType) {
            1 -> helper.setText(R.id.tv_item_card_type, "卡片")
            2 -> helper.setText(R.id.tv_item_card_type, "长文")
        }
    }

    private fun initCardBagView(helper: BaseViewHolder, bean: SearchResultBean) {
        val item = bean.subjectBean
        val name = helper.getView<TextView>(R.id.tv_item_subject_name)
        TextViewUtils.setTextViewColor(name,mKeyword,item.name)
        helper.itemView.setOnClickListener {
            mListener.subjectOnClick(item)
        }
        val cardList = item.cardList
        if (cardList.size == 0) return
        val viewPager = helper.getView<WrapContentHeightViewPager>(R.id.vp_item_subject)
        viewPager.clipToPadding = false
        viewPager.setPadding(ScreenUtils.dip2px(20f), 0, ScreenUtils.dip2px(20f), 0)
        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return if (cardList.size > 3) cardList.size / 3 else 1
            }

            override fun isViewFromObject(view: View, o: Any): Boolean {
                return view === o
            }

            override fun instantiateItem(container: ViewGroup, position: Int): LinearLayout {
                val subjectLayout = View.inflate(mContext, R.layout.layout_hot_subject, null) as LinearLayout
                for (i in 0..2) {
                    val index = position * 3 + i
                    if (index < cardList.size) {
                        val itemCardView = View.inflate(mContext, R.layout.item_hot_subject_card, null)
                        ImageLoader.load(mContext,cardList[index].imageUrl,R.drawable.default_header,itemCardView.iv_item_subject_card)
                        itemCardView.tv_item_subject_card_name.text = cardList[index].name
                        itemCardView.tv_item_subject_card_info.text =
                                mContext.getString(R.string.tv_card_info, DateTimeUtils.getDateString(cardList[index].date), cardList[index].author)
                        itemCardView.setOnClickListener {
                            mListener.cardOnClick(cardList[index])
                        }
                        subjectLayout.addView(itemCardView)
                        (itemCardView.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                                0, ScreenUtils.dip2px(15f),
                                0, ScreenUtils.dip2px(15f) )
                    }
                }
                container.addView(subjectLayout)
                return subjectLayout
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
    }

}