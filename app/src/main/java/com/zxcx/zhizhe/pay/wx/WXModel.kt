package com.zxcx.zhizhe.pay.wx

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/3/25
 * @Description :
 */
class WXModel(presenter: WXEntryContract.Presenter):BaseModel<WXEntryContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getWxOrderPay(circleId:Int){

        mDisposable = AppClient.getAPIService().getWxOrderPay(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<WXBean>(mPresenter){
                    override fun onNext(t: WXBean) {
                        mPresenter?.getWxOrderPaySuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}