package com.zxcx.shitang.ui.collect.cardCollect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CardCollectModel extends BaseModel {

    @NonNull
    private final CardCollectContract.Presenter mPresent;

    public CardCollectModel(@NonNull CardCollectContract.Presenter present) {
        this.mPresent = present;
    }
}


