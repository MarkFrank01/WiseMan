package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

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
}