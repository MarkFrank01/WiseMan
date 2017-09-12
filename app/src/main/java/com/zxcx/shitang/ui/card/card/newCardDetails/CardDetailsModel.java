package com.zxcx.shitang.ui.card.card.newCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;

public class CardDetailsModel extends BaseModel<CardDetailsContract.Presenter> {
    public CardDetailsModel(@NonNull CardDetailsContract.Presenter present) {
        this.mPresent = present;
    }

    public void getCardDetails(int userId, int cardId){
        subscription = AppClient.getAPIService().getCardDetails(userId, cardId)
                .compose(this.<BaseBean<CardDetailsBean>>io_main())
                .compose(this.<CardDetailsBean>handleResult())
                .subscribeWith(new BaseSubscriber<CardDetailsBean>(mPresent) {
                    @Override
                    public void onNext(CardDetailsBean bea) {
                        mPresent.getDataSuccess(bea);
                    }
                });
        addSubscription(subscription);
    }
}


