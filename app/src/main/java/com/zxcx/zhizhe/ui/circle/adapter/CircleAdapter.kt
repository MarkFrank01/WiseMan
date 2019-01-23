package com.zxcx.zhizhe.ui.circle.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleHomeOnClickListener
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleAdapter(data:List<CircleBean>, private val mListener: CircleHomeOnClickListener):
        BaseMultiItemQuickAdapter<CircleBean,BaseViewHolder>(data){
    init {
        addItemType(CircleBean.CIRCLE_HOME_1, R.layout.item_more_circle_list)
    }

    override fun convert(helper: BaseViewHolder, item: CircleBean) {

        val imageUrl = ZhiZheUtils.getHDImageUrl(item.titleImage)
        val imageView = helper.getView<ImageView>(R.id.iv_item_card_icon)
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView)

        helper.setText(R.id.tv_circle_title,item.title)
        helper.setText(R.id.tv_item_card_title,item.sign)
        helper.setText(R.id.tv_item_circle_input,""+item.joinUserCount)
        helper.setText(R.id.tv_item_circle_comment,""+item.qaCount)

        helper.setText(R.id.tv_circle_classify_title,item.classifytitle)
    }

}