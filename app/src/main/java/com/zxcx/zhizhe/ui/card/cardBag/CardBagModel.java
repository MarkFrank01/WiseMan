package com.zxcx.zhizhe.ui.card.cardBag;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class CardBagModel extends BaseModel<CardBagContract.Presenter> {
    public CardBagModel(@NonNull CardBagContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCardBagCardList(int id, int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCardBagCardList(id,page,pageSize)
                .compose(BaseRxJava.INSTANCE.<BaseArrayBean<CardBagBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CardBagBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CardBagBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
    
}


