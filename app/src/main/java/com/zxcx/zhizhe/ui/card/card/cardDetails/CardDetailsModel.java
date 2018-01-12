package com.zxcx.zhizhe.ui.card.card.cardDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.PostSubscriber;

import io.reactivex.subscribers.DisposableSubscriber;

public class CardDetailsModel extends BaseModel<CardDetailsContract.Presenter> {
    public CardDetailsModel(@NonNull CardDetailsContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCardDetails(int cardId){
        mDisposable = AppClient.getAPIService().getCardDetails(cardId)
                .compose(BaseRxJava.INSTANCE.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardDetailsBean>handleResult())
                .subscribeWith(new BaseSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void readArticle(int cardId){
        AppClient.getAPIService().readArticle(cardId)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .subscribeWith(new DisposableSubscriber<BaseBean>() {
                    @Override
                    public void onNext(BaseBean bean) {
                        //保持为空，不需要返回结果
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void likeCard(int cardId){
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(BaseRxJava.INSTANCE.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.likeSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().removeLikeCard(cardId)
                .compose(BaseRxJava.INSTANCE.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void unLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().unLikeCard(cardId)
                .compose(BaseRxJava.INSTANCE.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeUnLikeCard(int cardId){
        mDisposable = AppClient.getAPIService().removeUnLikeCard(cardId)
                .compose(BaseRxJava.INSTANCE.<BaseBean<CardDetailsBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<CardDetailsBean>handleResult())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void addCollectCard(int cardId) {
        mDisposable = AppClient.getAPIService().addCollectCard(cardId)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.collectSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void removeCollectCard(int cardId){
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new PostSubscriber<CardDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(CardDetailsBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void setUserFollow(int authorId, int followType){
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,followType)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.followSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


