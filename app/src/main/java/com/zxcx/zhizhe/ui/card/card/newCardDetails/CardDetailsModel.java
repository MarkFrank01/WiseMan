package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

public class CardDetailsModel extends BaseModel<CardDetailsContract.Presenter> {
    public CardDetailsModel(@NonNull CardDetailsContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCardDetails(int cardId){
        mDisposable = AppClient.getAPIService().getCardDetails(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new BaseSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bea) {
                        mPresenter.getDataSuccess(bea);
                    }
                });
        addSubscription(mDisposable);
    }

    public void likeCard(int cardId){
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void unLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().unLikeCard(cardId)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeCollectCard(int cardId){
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bea) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


