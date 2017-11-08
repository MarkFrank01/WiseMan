package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public class CardBagCardDetailsModel extends BaseModel<CardBagCardDetailsContract.Presenter> {
    public CardBagCardDetailsModel(@NonNull CardBagCardDetailsContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getAllCardId(int cardBagId) {
        mDisposable = AppClient.getAPIService().getAllCardId(cardBagId)
                .compose(BaseRxJava.<BaseArrayBean<CardBagCardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardBagCardDetailsBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CardBagCardDetailsBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CardBagCardDetailsBean> list) {
                        mPresenter.getAllCardIdSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void getCardDetails(int cardId){
        mDisposable = AppClient.getAPIService().getCardDetails(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new BaseSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void likeCard(int cardId){
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().removeLikeCard(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void unLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().unLikeCard(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeUnLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().removeUnLikeCard(cardId)
                .compose(BaseRxJava.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeCollectCard(int cardId){
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.UnCollectSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


