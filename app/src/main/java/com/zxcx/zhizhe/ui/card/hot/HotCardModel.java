package com.zxcx.zhizhe.ui.card.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.Date;
import java.util.List;

public class HotCardModel extends BaseModel<HotCardContract.Presenter> {

    public HotCardModel(@NonNull HotCardContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getHotCard(Date lastRefresh,int page){
        mDisposable = AppClient.getAPIService().getHotCard(lastRefresh,page)
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new BaseSubscriber<List<CardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


