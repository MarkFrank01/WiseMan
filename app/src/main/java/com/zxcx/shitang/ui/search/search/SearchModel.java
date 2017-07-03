package com.zxcx.shitang.ui.search.search;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class SearchModel extends BaseModel<SearchContract.Presenter> {
    public SearchModel(@NonNull SearchContract.Presenter present) {
        this.mPresent = present;
    }
}


