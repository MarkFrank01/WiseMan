package com.zxcx.zhizhe.ui.my.money

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MyMoneyModel (presenter:MyMoneyContract.Presenter):BaseModel<MyMoneyContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }


    fun getAccountDetails(){
        mDisposable = AppClient.getAPIService().accountDetails
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<MoneyBean>(mPresenter){
                    override fun onNext(t: MoneyBean) {
                        mPresenter?.getAccountDetailsSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}