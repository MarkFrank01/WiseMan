package com.zxcx.zhizhe.ui.search.result

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class SearchResultModel(presenter: SearchResultContract.Presenter) : BaseModel<SearchResultContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun search(keyword: String, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().search(keyword, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<SearchResultBean>>(mPresenter) {
                    override fun onNext(list: MutableList<SearchResultBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


