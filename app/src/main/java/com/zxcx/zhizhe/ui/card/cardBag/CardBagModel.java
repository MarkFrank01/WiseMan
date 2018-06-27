package com.zxcx.zhizhe.ui.card.cardBag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.mvpBase.BaseModel;

public class CardBagModel extends BaseModel<CardBagContract.Presenter> {
    public CardBagModel(@NonNull CardBagContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCardBagCardList(int id, int page, int pageSize){
        /*mDisposable = AppClient.getAPIService().getCardBagCardList(id,page,pageSize)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);*/
    }
    
}


