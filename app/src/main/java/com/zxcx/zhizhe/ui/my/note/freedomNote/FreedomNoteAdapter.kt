package com.zxcx.zhizhe.ui.my.note.freedomNote

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.card.NoteBean

/**
 * Created by anm on 2017/12/1.
 */
class FreedomNoteAdapter(data : List<NoteBean>) : BaseQuickAdapter<NoteBean, BaseViewHolder>(R.layout.item_card_note,data){

    override fun convert(helper: BaseViewHolder, item: NoteBean) {
        helper.setText(R.id.tv_item_card_note_name,item.name)
        helper.setText(R.id.tv_item_card_note_info, item.content)
    }

}