package com.zxcx.zhizhe.ui.home.attention;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.CardBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class AttentionCardAdapter extends BaseQuickAdapter<CardBean,BaseViewHolder> {

    public AttentionCardAdapter(@Nullable List<CardBean> data) {
        super(R.layout.item_card,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardBean item) {
        ImageView imageView = helper.getView(R.id.iv_item_card_icon);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);

        helper.setText(R.id.tv_item_card_title,item.getName());
        helper.setText(R.id.tv_item_card_card_bag,item.getCardBagName());
        helper.setText(R.id.tv_item_card_reade_num,item.getReadNum()+"");
        helper.setText(R.id.tv_item_card_collect_num,item.getCollectNum()+"");
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
