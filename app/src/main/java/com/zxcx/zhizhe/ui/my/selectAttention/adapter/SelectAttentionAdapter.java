package com.zxcx.zhizhe.ui.my.selectAttention.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SelectAttentionAdapter extends BaseQuickAdapter<SelectAttentionBean,BaseViewHolder> {

    public SelectAttentionAdapter(@Nullable List<SelectAttentionBean> data) {
        super(R.layout.item_select_attention, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SelectAttentionBean item) {
        if (item.isChecked()){
            helper.setVisible(R.id.iv_item_select_attention_select,true);
        }else {
            helper.setVisible(R.id.iv_item_select_attention_select,false);
        }

        ViewGroup.LayoutParams para = helper.itemView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.width = (screenWidth - ScreenUtils.dip2px(12*2) - ScreenUtils.dip2px(10*2)) / 3;
        para.height = (screenWidth - ScreenUtils.dip2px(12*2) - ScreenUtils.dip2px(10*2)) / 3;
        helper.itemView.setLayoutParams(para);

        helper.setText(R.id.tv_item_select_attention_name,item.getName());
        RoundedImageView imageView = helper.getView(R.id.iv_item_select_attention_icon);
        ImageLoader.load(mContext,item.getImageUrl(),R.mipmap.image_morenlogo,imageView);
    }
}
