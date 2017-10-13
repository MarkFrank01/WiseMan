package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class SearchModel extends BaseModel<SearchContract.Presenter> {
    public SearchModel(@NonNull SearchContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getSearchHot(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getSearchHot(page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<SearchBean>>io_main())
                .compose(BaseRxJava.<SearchBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchBean>>(mPresenter) {
                    @Override
                    public void onNext(List<SearchBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


