package com.zxcx.shitang.ui.my.collect.collectCard;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CollectCardModel extends BaseModel<CollectCardContract.Presenter> {
    public CollectCardModel(@NonNull CollectCardContract.Presenter present) {
        this.mPresent = present;
    }
}


