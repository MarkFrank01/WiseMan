package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.bean.CheckBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleNameModel(presenter: CreateCircleNamePresenter):BaseModel<CreateCircleNameContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun checkCircleName(name:String){
        mDisposable = AppClient.getAPIService().getCheckName(name)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CheckBean>(mPresenter){
                    override fun onNext(t: CheckBean) {
                        mPresenter?.checkCanUse()
                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.checkSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}