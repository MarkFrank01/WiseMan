package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class ReadCardsModel(presenter: ReadCardsContract.Presenter) : BaseModel<ReadCardsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getReadCard(sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getReadCard(sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<ReadCardsBean>>(mPresenter) {
                    override fun onNext(list: List<ReadCardsBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


