package com.zxcx.shitang.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class AttentionModel extends BaseModel {

    @NonNull
    private final AttentionContract.Presenter mPresent;

    public AttentionModel(@NonNull AttentionContract.Presenter present) {
        this.mPresent = present;
    }
}


