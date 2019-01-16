package com.zxcx.zhizhe.ui.my.selectAttention.interest

import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.utils.ScreenUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SelectAttentionNewAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(ClassifyCardBean.TYPE_CARD_BAG, R.layout.item_select_card_new)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            ClassifyBean.TYPE_CLASSIFY -> {
                val bean = item as ClassifyBean
                val title = helper.getView<TextView>(R.id.tv_item_classify_classify)
                title.text = bean.title
                val lp = title.layoutParams as LinearLayout.LayoutParams
                lp.setMargins(0, ScreenUtils.dip2px(if (helper.adapterPosition == 0) 30f else 15f),
                        0, ScreenUtils.dip2px(15f))
                title.layoutParams = lp
            }
            ClassifyCardBean.TYPE_CARD_BAG -> {
                helper.addOnClickListener(R.id.fl_item_select_card_bag)
                val para = helper.itemView.layoutParams
                val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
                para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 4
                helper.itemView.layoutParams = para

                val cardBean = item as ClassifyCardBean
                helper.setText(R.id.cb_item_select_card_bag, cardBean.name)
                helper.setChecked(R.id.cb_item_select_card_bag, cardBean.isChecked)
            }
        }
    }

}