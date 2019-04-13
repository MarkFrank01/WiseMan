package com.zxcx.zhizhe.ui.my.money.editcount

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class EditMyCountModel(presenter:EditMyCountContract.Presenter) :BaseModel<EditMyCountContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun bindingAlipay(withdrawalAmount:String,contact:String,phone:String){
       mDisposable = AppClient.getAPIService().bindingAlipay(withdrawalAmount, contact, phone)
               .compose(BaseRxJava.io_main())
               .compose(BaseRxJava.handlePostResult())
               .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                   override fun onNext(t: BaseBean<*>?) {
                       mPresenter?.bindingAlipaySuccess()
                   }
               })
        addSubscription(mDisposable)
    }
}