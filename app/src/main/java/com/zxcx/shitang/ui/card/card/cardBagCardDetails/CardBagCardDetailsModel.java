package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardBagCardDetailsModel extends BaseModel<CardBagCardDetailsContract.Presenter> {
    public CardBagCardDetailsModel(@NonNull CardBagCardDetailsContract.Presenter present) {
        this.mPresent = present;
    }
}


