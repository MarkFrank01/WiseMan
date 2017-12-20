package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class DynamicMessageListModel(presenter: DynamicMessageListContract.Presenter) : BaseModel<DynamicMessageListContract.Presenter>() {



    init {
        this.mPresenter = presenter
    }

    fun getDynamicMessageList(messageType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getDynamicMessageList(messageType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<DynamicMessageListBean>>(mPresenter) {
                    override fun onNext(list: List<DynamicMessageListBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

}


