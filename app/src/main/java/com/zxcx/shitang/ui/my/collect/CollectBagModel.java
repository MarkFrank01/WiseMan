package com.zxcx.shitang.ui.my.collect;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CollectBagModel extends BaseModel<CollectBagContract.Presenter> {
    public CollectBagModel(@NonNull CollectBagContract.Presenter present) {
        this.mPresent = present;
    }
}


