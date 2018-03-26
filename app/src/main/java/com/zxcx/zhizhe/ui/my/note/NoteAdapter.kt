package com.zxcx.zhizhe.ui.my.note

import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.likeCards.SwipeMenuClickListener

/**
 * Created by anm on 2017/12/1.
 */
class NoteAdapter(data : List<NoteBean>) : BaseQuickAdapter<NoteBean, BaseViewHolder>(R.layout.item_note,data){

    lateinit var mListener: SwipeMenuClickListener

    init {
        setHeaderAndEmpty(true)
    }

    override fun convert(helper: BaseViewHolder, item: NoteBean) {
        //todo 笔记列表
        when (item.noteType) {
            1 -> {}
            2 -> {}
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
        helper.getView<FrameLayout>(R.id.content_view).setOnClickListener {
            easySwipeMenuLayout.resetStatus()
            mListener.onContentClick(mData.indexOf(item))
        }
    }
}