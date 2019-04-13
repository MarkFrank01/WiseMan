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
class CircleAnswerChildModel(presenter: CircleAnswerChildContract.Presenter):BaseModel<CircleAnswerChildContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun createAnswer(circleId:Int,qaId:Int,qaCommentId:Int,description:String){
        mDisposable = AppClient.getAPIService().createAnswerHaveKid(circleId, qaId,qaCommentId,description)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<QuestionBean>(mPresenter){
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