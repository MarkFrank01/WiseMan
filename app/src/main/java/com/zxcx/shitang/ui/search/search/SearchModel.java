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

    public void getSearchHot(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getSearchHot(page,pageSize)
                .compose(this.<BaseArrayBean<SearchBean>>io_main())
                .compose(this.<SearchBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchBean>>(mPresent) {
                    @Override
                    public void onNext(List<SearchBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


