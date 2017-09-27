package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.BaseRxJava;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.NullPostSubscriber;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public class CardBagCardDetailsModel extends BaseModel<CardBagCardDetailsContract.Presenter> {
    public CardBagCardDetailsModel(@NonNull CardBagCardDetailsContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAllCardId(int cardBagId) {
        mDisposable = AppClient.getAPIService().getAllCardId(cardBagId)
                .compose(BaseRxJava.<BaseArrayBean<Integer>>io_main())
                .compose(BaseRxJava.<Integer>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<Integer>>(mPresent) {
                    @Override
                    public void onNext(List<Integer> list) {
                        mPresent.getAllCardIdSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void getCardDetails(int cardId){
        mDisposable = AppClient.getAPIService().getCardDetails(cardId)
                .compose(this.<BaseBean<CardDetailsBean>>io_main())
                .compose(this.<CardDetailsBean>handleResult())
                .subscribeWith(new BaseSubscriber<CardDetailsBean>(mPresent) {
                    @Override
                    public void onNext(CardDetailsBean bea) {
                        mPresent.getDataSuccess(bea);
                    }
                });
        addSubscription(mDisposable);
    }

    public void likeCard(int cardId){
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void unLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().unLikeCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeCollectCard(int cardId){
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


