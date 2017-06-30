package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class CollectFolderModel extends BaseModel<CollectFolderContract.Presenter> {
    public CollectFolderModel(@NonNull CollectFolderContract.Presenter present) {
        this.mPresent = present;
    }
}


