package com.zxcx.zhizhe.ui.circle.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.utils.ImageLoader

/**
 * @author : MarkFrank01
 * @Created on 2019/2/21
 * @Description :
 */
class CircleDetaileAdapter(data: List<CircleDetailBean>) : BaseMultiItemQuickAdapter<CircleDetailBean, BaseViewHolder>(data) {


    init {
        addItemType(1, R.layout.item_circle_detail_huati)
    }

    override fun convert(helper: BaseViewHolder, item: CircleDetailBean) {
        val itemBean = item as CircleDetailBean

        val imageView = helper.getView<RoundedImageView>(R.id.circle_detail_img)
        ImageLoader.load(mContext, itemBean.usersVO.avater, R.drawable.default_card, imageView)

        helper.setText(R.id.circle_detail_text, itemBean.title)
        helper.setText(R.id.circle_detail_content, itemBean.description)
    }


}