package com.zxcx.shitang.ui.classify;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class ClassifyModel extends BaseModel<ClassifyContract.Presenter> {

    public ClassifyModel(@NonNull ClassifyContract.Presenter present) {
        mPresent = present;
    }
}


