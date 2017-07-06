package com.zxcx.shitang.ui.card.card.cardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardDetailsModel extends BaseModel<CardDetailsContract.Presenter> {
    public CardDetailsModel(@NonNull CardDetailsContract.Presenter present) {
        this.mPresent = present;
    }
}


