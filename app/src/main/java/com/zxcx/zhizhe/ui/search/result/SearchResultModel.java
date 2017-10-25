package com.zxcx.zhizhe.ui.search.result;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class SearchResultModel extends BaseModel<SearchResultContract.Presenter> {
    public SearchResultModel(@NonNull SearchResultContract.Presenter present) {
        this.mPresenter = present;
    }

    public void searchCard(String keyword, int page, int pageSize){
        mDisposable = AppClient.getAPIService().searchCard(keyword,page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<SearchCardBean>>io_main())
                .compose(BaseRxJava.<SearchCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchCardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<SearchCardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void searchCardBag(String keyword){
        mDisposable = AppClient.getAPIService().searchCardBag(keyword,0,1000)
                .compose(BaseRxJava.<BaseArrayBean<SearchCardBagBean>>io_main())
                .compose(BaseRxJava.<SearchCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SearchCardBagBean>>(mPresenter) {
                    @Override
                    public void onNext(List<SearchCardBagBean> list) {
                        mPresenter.searchCardBagSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


