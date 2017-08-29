package com.zxcx.shitang.ui.search.search;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;

import java.util.List;

public class SearchModel extends BaseModel<SearchContract.Presenter> {
    public SearchModel(@NonNull SearchContract.Presenter present) {
        this.mPresent = present;
    }

    public void getSearchHot(){
        subscription = AppClient.getAPIService().getSearchHot()
                .compose(this.<BaseArrayBean<String>>io_main())
                .compose(this.<String>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<String>>(mPresent) {
                    @Override
                    public void onNext(List<String> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }
}


