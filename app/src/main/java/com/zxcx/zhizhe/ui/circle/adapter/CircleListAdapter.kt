package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleListAdapter(data:List<CircleBean>):BaseQuickAdapter<CircleBean,BaseViewHolder>(R.layout.item_circle_list_iten,data){

    override fun convert(helper: BaseViewHolder, item: CircleBean) {
        val imageView = helper.getView<ImageView>(R.id.circle_list_item_iv)
        val textView = helper.getView<TextView>(R.id.circle_list_item_tv)
    }

}