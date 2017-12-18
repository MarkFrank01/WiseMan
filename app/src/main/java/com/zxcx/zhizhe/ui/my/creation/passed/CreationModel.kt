package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class CreationModel(presenter: CreationContract.Presenter) : BaseModel<CreationContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getCreation(passType: Int, sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getCreation(passType,sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<CreationBean>>(mPresenter) {
                    override fun onNext(list: List<CreationBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getOtherUserCreation(id: Int, sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getOtherUserCreation(id,sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<CreationBean>>(mPresenter) {
                    override fun onNext(list: List<CreationBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


