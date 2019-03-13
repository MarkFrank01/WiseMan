package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateNextModel(presenter: OwnerCreateNextContract.Presenter):BaseModel<OwnerCreateNextContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun setCircleArticle(circleId:Int,auditArticleList:List<Int>,privateArticleList:List<Int>){
        mDisposable = AppClient.getAPIService().setCircleArticle(circleId, auditArticleList, privateArticleList)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String,levelType: Int,limitedTimeType :Int) {

        mDisposable = AppClient.getAPIService().createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CircleBean>(mPresenter){
                    override fun onNext(t: CircleBean) {
                        mPresenter?.createCircleSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}