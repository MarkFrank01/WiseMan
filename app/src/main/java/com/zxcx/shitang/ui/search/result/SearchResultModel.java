package com.zxcx.shitang.ui.search.result;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class SearchResultModel extends BaseModel<SearchResultContract.Presenter> {
    public SearchResultModel(@NonNull SearchResultContract.Presenter present) {
        this.mPresent = present;
    }
}


