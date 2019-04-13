package com.zxcx.zhizhe.ui.circle.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class MyCircleJoinAdapter (data:List<CircleBean>):
        BaseMultiItemQuickAdapter<CircleBean, BaseViewHolder>(data){

    init {
        addItemType(CircleBean.CIRCLE_HOME_1, R.layout.item_circle_classify)
    }

    override fun convert(helper: BaseViewHolder, item: CircleBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_item_circle_classify_icon)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.titleImage)
        ImageLoader.load(mContext,imageUrl, R.drawable.default_card,imageView)

        helper.setText(R.id.tv_item_circle_classify_title,item.title)
        helper.setText(R.id.tv_item_circle_classify_desc,item.sign)
        helper.setText(R.id.tv_item_circle_classify_join,"加入"+item.joinUserCount+"w")
        helper.setText(R.id.tv_item_circle_classify_topic,"话题"+item.qaCount+"w")

        helper.getView<CheckBox>(R.id.cb_item_select_join_circle).visibility = View.GONE

    }

}