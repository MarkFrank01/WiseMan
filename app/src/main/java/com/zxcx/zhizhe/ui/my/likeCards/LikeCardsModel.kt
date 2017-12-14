package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class LikeCardsModel(presenter: LikeCardsContract.Presenter) : BaseModel<LikeCardsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getLikeCard(sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getLikeCard(sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<LikeCardsBean>>(mPresenter) {
                    override fun onNext(list: List<LikeCardsBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


