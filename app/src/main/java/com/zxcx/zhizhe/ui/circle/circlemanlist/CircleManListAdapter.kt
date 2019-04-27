package com.zxcx.zhizhe.ui.circle.circlemanlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.*

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
//        helper.setChecked(R.id.cb_item_search_user_follow, item.isFollow)
        helper.getView<View>(R.id.cb_item_search_user_follow).expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        helper.addOnClickListener(R.id.cb_item_search_user_follow)

        helper.addOnClickListener(R.id.iv_item_search_user)
        helper.addOnClickListener(R.id.tv_item_search_user_name)


        when (item.followType) {
            0 -> {
                helper.setText(R.id.cb_item_search_user_follow,"关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.button_blue))
                helper.setChecked(R.id.cb_item_search_user_follow,false)
            }

            1->{
                helper.setText(R.id.cb_item_search_user_follow,"已关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.text_color_3))
                helper.setChecked(R.id.cb_item_search_user_follow,true)

            }

            2->{
                helper.setText(R.id.cb_item_search_user_follow,"互相关注")
                helper.setTextColor(R.id.cb_item_search_user_follow,mContext.getColorForKotlin(R.color.text_color_3))
                helper.setChecked(R.id.cb_item_search_user_follow,true)

            }
        }

        val imageVIP = helper.getView<ImageView>(R.id.iv_item_card_officials)
        if (item.authorAuthenticationType!=0&&item.authorAuthenticationType==1){
            imageVIP.visibility = View.VISIBLE
        }
    }

}
