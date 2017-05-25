package com.zxcx.shitang.ui.loginAndRegister.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class SelectAttentionModel extends BaseModel {

    @NonNull
    private final SelectAttentionContract.Presenter mPresent;

    public SelectAttentionModel(@NonNull SelectAttentionContract.Presenter present) {
        this.mPresent = present;
    }
}


