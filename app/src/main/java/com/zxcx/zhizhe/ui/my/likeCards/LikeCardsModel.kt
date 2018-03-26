package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class LikeCardsModel(presenter: LikeCardsContract.Presenter) : BaseModel<LikeCardsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getLikeCard(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getLikeCard(0, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<MyCardsBean>>(mPresenter) {
                    override fun onNext(list: List<MyCardsBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun deleteLikeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeLikeCard(cardId)
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


