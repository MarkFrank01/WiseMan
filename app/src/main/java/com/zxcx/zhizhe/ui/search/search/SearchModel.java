package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.room.AppDatabase;
import com.zxcx.zhizhe.room.SearchHistory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchModel extends BaseModel<SearchContract.Presenter> {
    public SearchModel(@NonNull SearchContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getSearchHot(){
        mDisposable = AppClient.getAPIService().getSearchHot()
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(new BaseSubscriber<List<SearchBean>>(mPresenter) {
                    @Override
                    public void onNext(List<SearchBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);

        Disposable disposable = AppDatabase.getInstance().mSearchHistoryDao().getAll()
                .compose(BaseRxJava.io_main())
                .subscribeWith(new DisposableSubscriber<List<SearchHistory>>() {
                    @Override
                    public void onNext(List<SearchHistory> list) {
                        if (list.size() > 0) {
                            mPresenter.getSearchHistorySuccess(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        mPresenter.getDataFail("数据库读取异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscription(disposable);
    }

    public void getSearchPre(String keyword){

        mDisposable = Flowable.just(keyword)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(s -> s.length() > 0)
                .switchMap(s -> AppClient.getAPIService().getSearchPre(keyword)
                        .compose(BaseRxJava.handleArrayResult()))
                .compose(BaseRxJava.io_main())
                .subscribeWith(new BaseSubscriber<List<String>>(mPresenter) {
                    @Override
                    public void onNext(List<String> list) {
                        mPresenter.getSearchPreSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


