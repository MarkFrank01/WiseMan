package com.zxcx.shitang.ui.card.cardBag.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.card.cardBag.CardBagBean;
import com.zxcx.shitang.utils.ImageLoader;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CardBagListAdapter extends BaseQuickAdapter<CardBagBean,BaseViewHolder> {
    public CardBagListAdapter(@Nullable List<CardBagBean> data) {
        super(R.layout.item_card_bag_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardBagBean item) {
        helper.setText(R.id.tv_item_card_bag_list_title,item.getName());
        RoundedImageView imageView = helper.getView(R.id.iv_item_card_bag_list_icon);
        ImageLoader.load(mContext,item.getImageUrl(),R.mipmap.image_morenlogo,imageView);
        helper.setText(R.id.tv_item_card_bag_list_collect_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));
    }
}
