package com.zxcx.zhizhe.ui.circle.circlemessage

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleMessageAdapter(data:List<MyCircleTabBean>):BaseQuickAdapter<MyCircleTabBean,BaseViewHolder>(R.layout.item_circle_message,data) {

    override fun convert(helper: BaseViewHolder?, item: MyCircleTabBean?) {
    }

}