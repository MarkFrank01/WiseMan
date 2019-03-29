package com.zxcx.zhizhe.ui.my.selectAttention.man

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectManAdapter (data:List<SearchUserBean>):BaseQuickAdapter<SearchUserBean,BaseViewHolder>(R.layout.item_now_select_man){

    override fun convert(helper: BaseViewHolder, item: SearchUserBean) {
//        val imageView_head = helper.getView<RoundedImageView>(R.id.iv_my_head)
//        ImageLoader.load(mContext,item.imageUrl,R.drawable.default_header,imageView_head)

//        helper.setText(R.id.iv_man_name,item.name)
        val imageView = helper.getView<RoundedImageView>(R.id.iv_my_head)
        ImageLoader.load(mContext,item.imageUrl,R.drawable.default_header,imageView)

        helper.setText(R.id.tv_my_nick_name,item.name)
        helper.setText(R.id.tv_my_lv,"Lv."+item.level)
        helper.setText(R.id.tv_my_signature,""+item.cardNum+"作品  "+item.fansNum+"粉丝数")

        if (item.followType == 0){
            helper.getView<ImageView>(R.id.iv_item_show_click).visibility = View.GONE
        }else if (item.followType == 1||item.followType == 2){
            helper.getView<ImageView>(R.id.iv_item_show_click).visibility = View.VISIBLE
        }

        LogCat.e("item Type is "+item.followType)

        helper.addOnClickListener(R.id.iv_my_head)
    }
}