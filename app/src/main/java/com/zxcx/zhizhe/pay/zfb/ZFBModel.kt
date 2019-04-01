package com.zxcx.zhizhe.pay.zfb

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.PostSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/1
 * @Description :
 */
class ZFBModel(presenter:ZFBContract.Presenter):BaseModel<ZFBContract.Presenter>() {

    init {
        this.mPresenter= presenter
    }

    fun getAlOrderPayForJoinCircle(circleId:Int){
        mDisposable = AppClient.getAPIService().getAlOrderPayForJoinCircle(circleId)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :PostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>) {
//                        LogCat.e("??????"+t.data)
                        mPresenter?.getAlOrderPayForJoinCircle(t.data as String)
                    }
                })
        addSubscription(mDisposable)
    }
}