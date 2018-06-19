package com.zxcx.zhizhe.ui.card.cardBag;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.home.hot.HotBean;

import java.util.List;

public class CardBagModel extends BaseModel<CardBagContract.Presenter> {
    public CardBagModel(@NonNull CardBagContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCardBagCardList(int id, int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCardBagCardList(id,page,pageSize)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotBean>>(mPresenter) {
                    @Override
                    public void onNext(List<HotBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
    
}


