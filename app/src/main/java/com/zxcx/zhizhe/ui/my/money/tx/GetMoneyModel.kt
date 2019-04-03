package com.zxcx.zhizhe.ui.my.money.tx

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class GetMoneyModel(presenter:GetMoneyContract.Presenter):BaseModel<GetMoneyContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun applyForWithdrawal(money:String){
        mDisposable = AppClient.getAPIService().applyForWithdrawal(money)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.applyForWithdrawalSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}