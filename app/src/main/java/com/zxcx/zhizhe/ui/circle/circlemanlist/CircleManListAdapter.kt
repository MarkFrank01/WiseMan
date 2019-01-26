package com.zxcx.zhizhe.ui.circle.circlemanlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.expandViewTouchDelegate

/**
 * @author : MarkFrank01
 * @Created on 2019/1/26
 * @Description :
 */
class CircleManListAdapter(data:List<SearchUserBean>) :BaseQuickAdapter<SearchUserBean,BaseViewHolder>(R.layout.item_circle_man_item){

    override fun convert(helper: BaseViewHolder, item: SearchUserBean) {
        val title = helper.getView<TextView>(R.id.tv_item_search_user_name)
        title.text = item.name
        val imageView = helper.getView<RoundedImageView>(R.id.iv_item_search_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.imageUrl)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)
        helper.setText(R.id.tv_item_search_user_card, item.cardNum.toString())
        helper.setText(R.id.tv_item_search_user_fans, item.fansNum.toString())
        helper.setText(R.id.tv_item_search_user_like, item.likeNum.toString())
        helper.setText(R.id.tv_item_search_user_collect, item.collectNum.toString())
        helper.setText(R.id.tv_item_search_user_level, mContext.getString(R.string.tv_level, item.level))
        helper.setChecked(R.id.cb_item_search_user_follow, item.isFollow)
        helper.getView<View>(R.id.cb_item_search_user_follow).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        helper.addOnClickListener(R.id.cb_item_search_user_follow)

        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)
        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType==1){
            imageVIP.visibility = View.VISIBLE
        }
    }

}
