package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerModel(presenter:CircleAnswerContract.Presenter):BaseModel<CircleAnswerContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }


    fun createAnswer(circleId:Int,qaId:Int,description:String,askImage:List<String>){
        mDisposable = AppClient.getAPIService().createAnswerNokid(circleId, qaId,  description, askImage)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<QuestionBean>(mPresenter){
                    override fun onNext(t: QuestionBean) {
                        mPresenter?.createAnswerSuccess()
                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.createAnswerSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}