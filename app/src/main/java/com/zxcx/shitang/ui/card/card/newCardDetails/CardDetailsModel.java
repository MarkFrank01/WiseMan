package com.zxcx.shitang.ui.card.card.newCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

public class CardDetailsModel extends BaseModel<CardDetailsContract.Presenter> {
    public CardDetailsModel(@NonNull CardDetailsContract.Presenter present) {
        this.mPresent = present;
    }

    public void getCardDetails(int cardId){
        subscription = AppClient.getAPIService().getCardDetails(cardId)
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

    public void likeCard(int cardId){
        subscription = AppClient.getAPIService().likeCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }

    public void unLikeCard(int cardId){
        subscription = AppClient.getAPIService().unLikeCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }

    public void removeCollectCard(int cardId){
        subscription = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }
}


