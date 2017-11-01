package com.zxcx.zhizhe.ui.card.cardBag.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CardBagCardAdapter extends BaseQuickAdapter<CardBagBean,BaseViewHolder> {
    public CardBagCardAdapter( @Nullable List<CardBagBean> data) {
        super(R.layout.item_card_bag_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardBagBean item) {
        RoundedImageView imageView = helper.getView(R.id.iv_item_card_bag_card_icon);
        RelativeLayout relativeLayout = helper.getView(R.id.rl_item_card_bag_card);
        ViewGroup.LayoutParams para = relativeLayout.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(12 * 2) - ScreenUtils.dip2px(15)) / 2 * 3/4;
        relativeLayout.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);
        helper.setText(R.id.tv_item_card_bag_card_title,item.getName());
        helper.setText(R.id.tv_item_card_bag_card_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));
    }
}
