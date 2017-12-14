package com.zxcx.zhizhe.ui.search.result.card

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.DateTimeUtils

/**
 * Created by anm on 2017/12/1.
 */
class CardNoteAdapter(data : List<CardNoteBean>) : BaseQuickAdapter<CardNoteBean, BaseViewHolder>(R.layout.item_card_note,data){

    override fun convert(helper: BaseViewHolder, item: CardNoteBean) {
        helper.setText(R.id.tv_item_card_note_name,item.name)
        helper.setText(R.id.tv_item_card_note_info, mContext.getString(R.string.tv_item_card_note_info, DateTimeUtils.getDateString(item.date), item.content))
    }

}