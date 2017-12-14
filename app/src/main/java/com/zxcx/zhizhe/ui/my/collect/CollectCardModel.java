package com.zxcx.zhizhe.ui.my.collect;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class CollectCardModel extends BaseModel<CollectCardContract.Presenter> {
    public CollectCardModel(@NonNull CollectCardContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCollectCard(int sortType, int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCollectCard(sortType,page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<CollectCardBean>>io_main())
                .compose(BaseRxJava.<CollectCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectCardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CollectCardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


