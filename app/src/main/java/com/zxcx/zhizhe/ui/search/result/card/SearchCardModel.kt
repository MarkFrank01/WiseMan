package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class SearchCardModel(presenter: SearchCardContract.Presenter) : BaseModel<SearchCardContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun searchCard(keyword: String, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().searchCard(keyword, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<SearchCardBean>>(mPresenter) {
                    override fun onNext(list: List<SearchCardBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


