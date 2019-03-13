package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailModel(presenter:CircleQuestionDetailContract.Presenter):BaseModel<CircleQuestionDetailContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getQuestionInfo(qaId:Int){

        mDisposable = AppClient.getAPIService().getQuestionInfo(qaId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CircleDetailBean>(mPresenter){
                    override fun onNext(t: CircleDetailBean) {
                        mPresenter?.getBasicQuestionSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}