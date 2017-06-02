package com.zxcx.shitang.ui.collect.cardCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardCollectModel extends BaseModel<CardCollectContract.Presenter> {

    public CardCollectModel(@NonNull CardCollectContract.Presenter present) {
        this.mPresent = present;
    }
}


