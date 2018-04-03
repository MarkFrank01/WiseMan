package com.zxcx.zhizhe.ui.my.note

import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.likeCards.SwipeMenuClickListener
import com.zxcx.zhizhe.utils.DateTimeUtils

/**
 * Created by anm on 2017/12/1.
 */
class NoteAdapter(data : List<NoteBean>) : BaseQuickAdapter<NoteBean, BaseViewHolder>(R.layout.item_note,data){

    lateinit var mListener: SwipeMenuClickListener

    init {
        setHeaderAndEmpty(true)
    }

    override fun convert(helper: BaseViewHolder, item: NoteBean) {
        helper.setText(R.id.tv_item_note_name,item.name)
        helper.setText(R.id.tv_item_note_content,item.content)
        when (item.noteType) {
            0 -> {
                //自由笔记
                helper.setText(R.id.tv_item_note_info,mContext.getString(R.string.tv_item_card_note_info,
                        DateTimeUtils.getDateTimeString(item.date),"记录"))
            }
            1 -> {
                //卡片笔记
                helper.setText(R.id.tv_item_note_info,mContext.getString(R.string.tv_item_card_note_info,
                        DateTimeUtils.getDateTimeString(item.date),"摘录"))
            }
        }

        val easySwipeMenuLayout = helper.getView<EasySwipeMenuLayout>(R.id.es)
        helper.getView<TextView>(R.id.tv_cancel).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
        }
        helper.getView<TextView>(R.id.tv_delete).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
            mListener.onDeleteClick(mData.indexOf(item))
            remove(mData.indexOf(item))
        }
        helper.getView<LinearLayout>(R.id.content_view).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
            mListener.onContentClick(mData.indexOf(item))
        }
    }
}