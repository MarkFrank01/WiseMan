package com.zxcx.zhizhe.ui.my.money.bill

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillModel(presenter:BillContract.Presenter):BaseModel<BillContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    //获取账单
    fun getBillingDetails(page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getBillingDetails(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<BillBean>>(mPresenter){
                    override fun onNext(t: MutableList<BillBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取提现明细
    fun getCashWithdrawalDetails(page: Int,pageSize: Int){
        mDisposable = AppClient.getAPIService().getCashWithdrawalDetails(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<BillBean>>(mPresenter){
                    override fun onNext(t: MutableList<BillBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}