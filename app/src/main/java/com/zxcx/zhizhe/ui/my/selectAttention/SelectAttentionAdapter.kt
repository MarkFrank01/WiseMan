package com.zxcx.zhizhe.ui.my.selectAttention

import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.classify.ClassifyBean
import com.zxcx.zhizhe.ui.classify.ClassifyCardBagBean
import com.zxcx.zhizhe.utils.ScreenUtils



/**
 * Created by anm on 2017/5/23.
 */

class SelectAttentionAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_attention_classify_classify)
        addItemType(ClassifyCardBagBean.TYPE_CARD_BAG, R.layout.item_select_card_bag)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            ClassifyBean.TYPE_CLASSIFY -> {
                val bean = item as ClassifyBean
                val title = helper.getView<TextView>(R.id.tv_item_classify_classify)
                title.text = bean.title
                val paint = title.paint
                paint.isFakeBoldText = true
                if (helper.adapterPosition == 0){
                    val lp = title.layoutParams as LinearLayout.LayoutParams
                    lp.setMargins(0, ScreenUtils.dip2px(16f), 0, 0)
                    title.layoutParams = lp
                }
            }
            ClassifyCardBagBean.TYPE_CARD_BAG -> {
                helper.addOnClickListener(R.id.fl_item_select_card_bag)
                val para = helper.itemView.layoutParams
                val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
                para.width = (screenWidth - ScreenUtils.dip2px((15 * 2).toFloat()) - ScreenUtils.dip2px((20 * 2).toFloat())) / 3
                helper.itemView.layoutParams = para

                val cardBagBean = item as ClassifyCardBagBean
                helper.setText(R.id.cb_item_select_card_bag, cardBagBean.name)
                helper.setChecked(R.id.cb_item_select_card_bag, cardBagBean.isChecked)
            }
        }
    }
}
