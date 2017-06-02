package com.zxcx.shitang.ui.collect.cardBagCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardBagCollectModel extends BaseModel<CardBagCollectContract.Presenter> {

    public CardBagCollectModel(@NonNull CardBagCollectContract.Presenter present) {
        this.mPresent = present;
    }
}


