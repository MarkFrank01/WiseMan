package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class SearchUserModel(presenter: SearchUserContract.Presenter) : BaseModel<SearchUserContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun searchUser(keyword: String, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().searchUser(keyword, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<SearchUserBean>>(mPresenter) {
                    override fun onNext(list: List<SearchUserBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


