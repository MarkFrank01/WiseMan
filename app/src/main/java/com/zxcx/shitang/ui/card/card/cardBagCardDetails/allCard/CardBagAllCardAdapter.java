package com.zxcx.shitang.ui.card.card.cardBagCardDetails.allCard;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.card.card.cardBagCardDetails.CardBagCardDetailsBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CardBagAllCardAdapter extends BaseQuickAdapter<CardBagCardDetailsBean,BaseViewHolder> {

    private int cardId;

    public CardBagAllCardAdapter(@Nullable List<CardBagCardDetailsBean> data, int cardId) {
        super(R.layout.item_card_bag_all_card, data);
        this.cardId = cardId;
    }

    @Override
    protected void convert(BaseViewHolder helper, CardBagCardDetailsBean item) {
        helper.setText(R.id.tv_card_bag_all_card_num,helper.getAdapterPosition()+"");
        helper.setText(R.id.tv_card_bag_all_card_name,item.getTitle());
        if (cardId == item.getId()){
            helper.setTextColor(R.id.tv_card_bag_all_card_name, ContextCompat.getColor(mContext,R.color.button_blue));
        }

        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1){
            helper.setVisible(R.id.view_line,false);
        }
    }
}
