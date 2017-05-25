package com.zxcx.shitang.ui.allCardBag;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class AllCardBagModel extends BaseModel {

    @NonNull
    private final AllCardBagContract.Presenter mPresent;

    public AllCardBagModel(@NonNull AllCardBagContract.Presenter present) {
        this.mPresent = present;
    }
}


