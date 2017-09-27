package com.zxcx.shitang.ui.search.result;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;

import java.util.List;

public class SearchResultModel extends BaseModel<SearchResultContract.Presenter> {
    public SearchResultModel(@NonNull SearchResultContract.Presenter present) {
        this.mPresent = present;
    }

    public void searchCard(String keyword, int page, int pageSize){
        mDisposable = AppClient.getAPIService().searchCard(keyword,page,pageSize)
                .compose(this.<BaseArrayBean<SearchCardBean>>io_main())
                .compose(this.<SearchCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchCardBean>>(mPresent) {
                    @Override
                    public void onNext(List<SearchCardBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void searchCardBag(String keyword){
        mDisposable = AppClient.getAPIService().searchCardBag(keyword,0,1000)
                .compose(this.<BaseArrayBean<SearchCardBagBean>>io_main())
                .compose(this.<SearchCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchCardBagBean>>(mPresent) {
                    @Override
                    public void onNext(List<SearchCardBagBean> list) {
                        mPresent.searchCardBagSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


