package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class SystemMessageModel(presenter: SystemMessageContract.Presenter) : BaseModel<SystemMessageContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getSystemMessage(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getSystemMessage(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<SystemMessageBean>>(mPresenter) {
                    override fun onNext(list: List<SystemMessageBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


