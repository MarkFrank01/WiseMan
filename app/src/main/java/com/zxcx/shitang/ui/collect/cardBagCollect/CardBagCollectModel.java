package com.zxcx.shitang.ui.collect.cardBagCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardBagCollectModel extends BaseModel {

    @NonNull
    private final CardBagCollectContract.Presenter mPresent;

    public CardBagCollectModel(@NonNull CardBagCollectContract.Presenter present) {
        this.mPresent = present;
    }
}


