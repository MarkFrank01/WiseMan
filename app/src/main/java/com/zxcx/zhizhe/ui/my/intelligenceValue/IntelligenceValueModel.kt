package com.zxcx.zhizhe.ui.my.intelligenceValue

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class IntelligenceValueModel(presenter: IntelligenceValueContract.Presenter) : BaseModel<IntelligenceValueContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getIntelligenceValue() {
        mDisposable = AppClient.getAPIService().getIntelligenceValue()
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<IntelligenceValueBean>(mPresenter) {
                    override fun onNext(bean: IntelligenceValueBean) {
                        mPresenter?.getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }
}


