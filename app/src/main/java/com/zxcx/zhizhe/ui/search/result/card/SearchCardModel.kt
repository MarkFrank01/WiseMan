package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants

class SearchCardModel(presenter: SearchCardContract.Presenter) : BaseModel<SearchCardContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun searchCard(keyword: String, cardType: Int, page: Int) {
        mDisposable = AppClient.getAPIService().searchCard(keyword, cardType, page, Constants.PAGE_SIZE)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter) {
                    override fun onNext(list: MutableList<CardBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


