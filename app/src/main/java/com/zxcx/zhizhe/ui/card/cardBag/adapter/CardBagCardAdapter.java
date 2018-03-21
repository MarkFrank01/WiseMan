package com.zxcx.zhizhe.ui.card.cardBag.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
        super(R.layout.item_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardBagBean item) {
        ImageView imageView = helper.getView(R.id.iv_item_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(20 * 2)) * 9/16;
        imageView.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);

        helper.setText(R.id.tv_item_card_title,item.getName());
        helper.setText(R.id.tv_item_card_card_bag,item.getCardBagName());
        helper.setText(R.id.tv_item_card_like,item.getLikeNum()+"");
        helper.setText(R.id.tv_item_card_read,item.getReadNum()+"");
        switch (item.getCardType()){
            case 1:
                helper.setText(R.id.tv_item_card_type,"卡片");
                break;
            case 2:
                helper.setText(R.id.tv_item_card_type,"长文");
                break;
        }
    }
}
