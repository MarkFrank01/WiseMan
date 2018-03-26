package com.zxcx.zhizhe.ui.search.result.subject

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.TextViewUtils
import com.zxcx.zhizhe.widget.WrapContentHeightViewPager
import kotlinx.android.synthetic.main.item_hot_subject_card.view.*

/**
 * Created by anm on 2017/12/1.
 */
class SearchSubjectAdapter(data : List<SubjectBean>,private val  mListener: SubjectOnClickListener) : BaseQuickAdapter<SubjectBean, BaseViewHolder>(R.layout.item_subject,data){

    var mKeyword = ""

    override fun convert(helper: BaseViewHolder, item: SubjectBean) {
        val name = helper.getView<TextView>(R.id.tv_item_subject_name)
        TextViewUtils.setTextViewColor(name,mKeyword,item.name)
        val cardList = item.cardList
        if (cardList.size == 0) return
        val viewPager = helper.getView<WrapContentHeightViewPager>(R.id.vp_item_subject)
        viewPager.clipToPadding = false
        viewPager.setPadding(ScreenUtils.dip2px(20f), 0, ScreenUtils.dip2px(20f), 0)
        viewPager.pageMargin = ScreenUtils.dip2px(10f)
        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return cardList.size / 3 + 1
            }

            override fun isViewFromObject(view: View, o: Any): Boolean {
                return view === o
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val subjectLayout = View.inflate(mContext, R.layout.layout_hot_subject, null) as LinearLayout
                for (i in 0..2) {
                    val index = position * 3 + i
                    if (index < count) {
                        val itemCardView = View.inflate(mContext, R.layout.item_hot_subject_card, null)
                        ImageLoader.load(mContext,cardList[index].imageUrl,R.drawable.default_header,itemCardView.iv_item_subject_card)
                        TextViewUtils.setTextViewColor(itemCardView.tv_item_subject_card_name,mKeyword,cardList[index].name)
                        itemCardView.tv_item_subject_card_info.text =
                                mContext.getString(R.string.tv_card_info, DateTimeUtils.getDateString(cardList[index].date), cardList[index].author)
                        itemCardView.setOnClickListener {
                            mListener.cardOnClick(cardList[index])
                        }
                        subjectLayout.addView(itemCardView)
                    }
                }
                subjectLayout.setOnClickListener {
                    mListener.subjectOnClick(item)
                }
                return subjectLayout
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
    }
}