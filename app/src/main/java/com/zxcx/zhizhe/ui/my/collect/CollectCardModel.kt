package com.zxcx.zhizhe.ui.my.collect

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean

class CollectCardModel(present: CollectCardContract.Presenter) : BaseModel<CollectCardContract.Presenter>() {
    init {
        this.mPresenter = present
    }

    fun getEmptyRecommendCard() {
        mDisposable = AppClient.getAPIService().getEmptyRecommendCard(2)
                .compose(BaseRxJava.io_main())
                .compose<CardBean>(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.getEmptyRecommendCardSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getCollectCard(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getCollectCard(0, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose<List<MyCardsBean>>(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<MyCardsBean>>(mPresenter) {
                    override fun onNext(list: List<MyCardsBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun deleteCollectCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}


