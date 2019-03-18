package com.zxcx.zhizhe.ui.circle.circlequestion

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionModel(presenter:CircleQuestionContract.Presenter):BaseModel<CircleQuestionContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    fun pushQuestion(circleId:Int,title:String,description:String,askImage:List<String>){
        mDisposable = AppClient.getAPIService().pushQuestion(circleId,title, description, askImage)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<QuestionBean>(mPresenter){
                    override fun onNext(t: QuestionBean) {
                        mPresenter?.pushQuestionSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}