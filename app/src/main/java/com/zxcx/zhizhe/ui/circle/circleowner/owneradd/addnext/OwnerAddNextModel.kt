package com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
class OwnerAddNextModel(presenter: OwnerAddNextContract.Presenter) : BaseModel<OwnerAddNextContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    //设置文章哦
    fun setCircleArticle(circleId: Int, auditArticleList: List<Int>, privateArticleList: List<Int>) {
        mDisposable = AppClient.getAPIService().setCircleArticle(circleId, auditArticleList, privateArticleList)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.setArcSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //检查圈子文章是否平衡
    fun checkCircleArticleBalance(circleId:Int){
        mDisposable = AppClient.getAPIService().checkCircleArticleBalance(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<BalanceBean>(mPresenter){
                    override fun onNext(t: BalanceBean) {
                        mPresenter?.checkCircleArticleBalanceSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}