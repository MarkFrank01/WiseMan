package com.zxcx.zhizhe.ui.home.attention.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.HotCardBagBean;
import com.zxcx.zhizhe.utils.ImageLoader;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class AttentionCardBagAdapter extends BaseQuickAdapter<HotCardBagBean,BaseViewHolder> {

    public AttentionCardBagAdapter(@Nullable List<HotCardBagBean> data) {
        super(R.layout.item_hot_card_bag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBagBean item) {
        helper.setText(R.id.tv_item_home_card_bag_title,item.getName());
        RoundedImageView imageView = helper.getView(R.id.iv_item_home_card_bag_icon);
        ImageLoader.load(mContext,item.getImageUrl(),R.mipmap.image_morenlogo,imageView);
    }
}
