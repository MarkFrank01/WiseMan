package com.zxcx.shitang.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class HotModel extends BaseModel<HotContract.Presenter> {

    public HotModel(@NonNull HotContract.Presenter present) {
        this.mPresent = present;
    }
}


