package com.zxcx.shitang.ui.allCardBag;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class AllCardBagModel extends BaseModel<AllCardBagContract.Presenter> {

    public AllCardBagModel(@NonNull AllCardBagContract.Presenter present) {
        mPresent = present;
    }
}


