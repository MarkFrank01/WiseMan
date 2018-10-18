package com.zxcx.zhizhe.ui.my.pastelink

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R

class PasteLinkAdapter(data:List<PastLinkBean>):BaseQuickAdapter<PastLinkBean,BaseViewHolder>(R.layout.item_past_link,data){

    init {
        setHeaderAndEmpty(true)
    }

    override fun convert(helper: BaseViewHolder?, item: PastLinkBean?) {

    }

}