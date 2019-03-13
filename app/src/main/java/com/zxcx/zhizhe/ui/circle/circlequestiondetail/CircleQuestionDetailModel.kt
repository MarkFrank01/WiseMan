package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.utils.Constants

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

    fun getAnswerList(qaId: Int,page:Int){
        mDisposable = AppClient.getAPIService().getAnswerList(qaId,page,Constants.PAGE_SIZE)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .map {
                    it.forEach {
                        it.addAll()
                    }
                    it
                }
                .subscribeWith(object :BaseSubscriber<MutableList<CircleCommentBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleCommentBean>) {
                        mPresenter?.getCommentBeanSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}