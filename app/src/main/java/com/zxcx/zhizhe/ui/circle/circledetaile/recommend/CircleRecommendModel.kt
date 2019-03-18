package com.zxcx.zhizhe.ui.circle.circledetaile.recommend

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/18
 * @Description :
 */
class CircleRecommendModel(presenter:CircleRecommendContract.Presenter):BaseModel<CircleRecommendContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    //推荐文章
    fun getArticleByCircleId(circleId:Int,orderType:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getArticleByCircleId(circleId, orderType, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.getRecommendArcSuccess(t)
                    }
                })
        addSubscription(mDisposable)

    }
}