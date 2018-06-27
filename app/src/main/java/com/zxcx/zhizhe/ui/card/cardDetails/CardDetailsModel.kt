package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import io.reactivex.subscribers.DisposableSubscriber

class CardDetailsModel(presenter: CardDetailsContract.Presenter) : BaseModel<CardDetailsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun readCard(cardId: Int) {
        val disposableSubscriber = AppClient.getAPIService().readArticle(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : DisposableSubscriber<BaseBean<*>>() {
                    override fun onNext(bean: BaseBean<*>) {
                        //保持为空，不需要返回结果
                    }

                    override fun onError(t: Throwable) {}

                    override fun onComplete() {}
                })
    }

    fun likeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.likeSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeLikeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeLikeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun unLikeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().unLikeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeUnLikeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeUnLikeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun addCollectCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().addCollectCard(cardId)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.collectSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeCollectCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun setUserFollow(authorId: Int, followType: Int,bean: CardBean) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId, followType)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(a: BaseBean<*>) {
                        mPresenter?.followSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }
}


