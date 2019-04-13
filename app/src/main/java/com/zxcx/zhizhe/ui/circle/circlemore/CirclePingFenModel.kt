package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/4
 * @Description :
 */
class CirclePingFenModel(presenter:CirclePingFenContract.Presenter):BaseModel<CirclePingFenContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun evaluationCircle(circleId:Int,contentQuality:Int,topicQuality:Int){
        mDisposable = AppClient.getAPIService().evaluationCircle(circleId, contentQuality, topicQuality)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.evaluationCircleSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}