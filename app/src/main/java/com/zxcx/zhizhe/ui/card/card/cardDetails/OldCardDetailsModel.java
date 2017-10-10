package com.zxcx.zhizhe.ui.card.card.cardDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;

public class OldCardDetailsModel extends BaseModel<OldCardDetailsContract.Presenter> {
    public OldCardDetailsModel(@NonNull OldCardDetailsContract.Presenter present) {
        this.mPresent = present;
    }
}


