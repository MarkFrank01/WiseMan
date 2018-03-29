package com.zxcx.zhizhe.ui.home.hot

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.subject.SubjectOnClickListener
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.WrapContentHeightViewPager
import kotlinx.android.synthetic.main.item_hot_subject_card.view.*

/**
 * Created by anm on 2017/6/26.
 */

class HotCardAdapter(data: List<HotBean>?, private val mListener: SubjectOnClickListener) : BaseMultiItemQuickAdapter<HotBean, BaseViewHolder>(data) {

    init {
        addItemType(HotBean.TYPE_CARD, R.layout.item_card)
        addItemType(HotBean.TYPE_SUBJECT, R.layout.item_subject)
    }

    override fun convert(helper: BaseViewHolder, item: HotBean) {
        when (helper.itemViewType) {
            HotBean.TYPE_CARD -> initCardView(helper, item)
            HotBean.TYPE_SUBJECT -> initCardBagView(helper, item)
        }

    }

    private fun initCardView(helper: BaseViewHolder, bean: HotBean) {
        val item = bean.cardBean
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)

        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_card, imageView)

        helper.setText(R.id.tv_item_card_title, item.name)
        helper.setText(R.id.tv_item_card_card_bag, item.cardBagName)
        helper.setText(R.id.tv_item_card_reade_num, item.readNum.toString())
        helper.setText(R.id.tv_item_card_collect_num, item.collectNum.toString())
        when (item.cardType) {
            1 -> helper.setText(R.id.tv_item_card_type, "卡片")
            2 -> helper.setText(R.id.tv_item_card_type, "长文")
        }
    }

    private fun initCardBagView(helper: BaseViewHolder, bean: HotBean) {
        val subjectBean = bean.subjectBean
        helper.setText(R.id.tv_item_subject_name,subjectBean.name)
        helper.itemView.setOnClickListener {
            mListener.subjectOnClick(subjectBean)
        }
        val cardList = subjectBean.cardList
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
